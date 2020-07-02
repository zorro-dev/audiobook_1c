package com.app.audiobook.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.audiobook.R;
import com.app.audiobook.audio.AudioLibraryManager;
import com.app.audiobook.component.Base;
import com.app.audiobook.component.JSONManager;
import com.app.audiobook.component.TimeManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;

public class AuthManager {

    private final String TAG = "AuthManager";

    public final static String AUTH_TYPE_ANONYMOUSLY = "anonymously";
    public final static String AUTH_TYPE_GOOGLE = "google";

    public static final int GOOGLE_SIGN_REQUEST = 101;

    private Context context;
    private GoogleSignInClient mGoogleSignInClient;
    private User user;
    private IAuthEvents events;
    private List<AuthCompleteListener> authCompleteListeners = new ArrayList<>();
    private List<StartLoadingUserListener> startLoadingUserListeners = new ArrayList<>();

    public void addAuthCompleteListener(AuthCompleteListener authCompleteListener) {
        authCompleteListeners.add(authCompleteListener);
    }

    public void removeAuthCompleteListener(AuthCompleteListener authCompleteListener) {
        authCompleteListeners.remove(authCompleteListener);
    }

    public void addStartLoadingUserListener(StartLoadingUserListener startLoadingUserListener) {
        startLoadingUserListeners.add(startLoadingUserListener);
    }

    public void removeStartLoadingUserListener(StartLoadingUserListener startLoadingUserListener) {
        startLoadingUserListeners.remove(startLoadingUserListener);
    }

    public AuthManager(Context context) {
        this.context = context;
        initGoogleAuthClient(context);
    }

    public void setEvents(IAuthEvents events) {
        this.events = events;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void initUser() {
        user = new User(String.valueOf(System.currentTimeMillis()));

        if (isUserAuthed()) {
            getUserFromDatabase(getFirebaseUser(), false, null);
        }/* else {
            singInAnonymously();
        }*/


    }

    private void initGoogleAuthClient(Context context) {
        // Configure Google Sign In
        Resources res = context.getResources();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(res.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
    }

    private void singInAnonymously() {
        if (events != null) {
            events.onStartAuth();
        }

        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            getUserFromDatabase(user, false, AUTH_TYPE_ANONYMOUSLY);
                        } else {
                            Toast.makeText(context, "Auth error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signInWithGoogle(GoogleSignInAccount acct) {
        if (Base.isOnline() == false) {
            Toast.makeText(context, "Проверьте соединение с интернетом", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);


            //  mAuth.getCurrentUser().linkWithCredential(credential)
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            getUserFromDatabase(user, true, AUTH_TYPE_GOOGLE);
                        } else {
                            //ошибка авторизации в firebase c помощью google

                            mAuth.signInWithCredential(credential)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            getUserFromDatabase(user, true, AUTH_TYPE_GOOGLE);
                                        } else {
                                            //ошибка авторизации в firebase c помощью google
                                            mGoogleSignInClient.signOut();
                                            Toast.makeText(context, "Error google auth", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });



        }
    }

    private User createUserInDatabase(FirebaseUser firebaseUser, boolean isVerified) {
        User user = new User(firebaseUser.getUid());

        user.getRegistryTime().setValue(TimeManager.getCurrentTimeString());
        user.getMail().setValue(firebaseUser.getEmail());
        user.getLastEnterTime().setValue(user.getRegistryTime().getValue());
        user.getGMT().setValue(timeZone());
        user.getUserType().setValue(User.TYPE_USER);

        String name;

        if (firebaseUser.getDisplayName() == null ||
                firebaseUser.getDisplayName().length() == 0) {
            //задать пользователю рандомное имя
            name = "user" + String.valueOf(System.currentTimeMillis());
        } else {
            //если пользователь имеет имя
            name = firebaseUser.getDisplayName();
        }

        user.getName().setValue(name);

        //user.isVerified = isVerified;

        user.syncWithDatabase();

        return user;
    }

    public void startAuthWithGoogle(Activity activity) {
        if (events != null) {
            events.onStartAuth();
        }
        startSignInIntent(activity);
    }

    private void getUserFromDatabase(FirebaseUser firebaseUser, boolean isVerified, String authType) {

        onStartLoadingUser();

        if (Base.isOnline()) {

            // добавление дефолтной книги
            String json = JSONManager.exportToJSON(AudioLibraryManager.getAudioBook4());
            FirebaseDatabase.getInstance().getReference("BookCatalog")
                    .child("ByUsers").child(firebaseUser.getUid())
                    .child(AudioLibraryManager.getAudioBook4().getId())
                    .setValue(json);

            FirebaseDatabase.getInstance().getReference("Users")
                    .child(firebaseUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                            User user;

                            boolean isUserAlreadyCreated = map != null;

                            if (isUserAlreadyCreated) {
                                user = new User(map);
                                setUser(user);

                                getUser().getLastEnterTime()
                                        .setValue(TimeManager.getCurrentTimeString())
                                        .syncWithDatabase();
                            } else {
                                user = createUserInDatabase(firebaseUser, isVerified);

                                setUser(user);
                            }

                            if (authType != null) {
                                getUser().getAuthType()
                                        .setValue(authType)
                                        .syncWithDatabase();
                            }

                            if (user.needToCreateOwnInviteCode()) {
                                user.createOwnInviteCode();
                            }

                            onAuthCompleted(user);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }

    }

    private void onAuthCompleted(User user) {
        if (events != null)
            events.onAuthCompleted(user);

        for (AuthCompleteListener listener : authCompleteListeners) {
            if (listener != null)
                listener.onAuthCompleted(user);
        }

    }

    private void onStartLoadingUser() {
        if (events != null)
            events.onStartLoadingUser();

        for (StartLoadingUserListener listener : startLoadingUserListeners) {
            if (listener != null)
                listener.onStartLoadingUser();
        }
    }

    private void startSignInIntent(Activity activity) {
        activity.startActivityForResult(mGoogleSignInClient.getSignInIntent(), GOOGLE_SIGN_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v("MainActivity", "onActivityResult");
        if (requestCode == GOOGLE_SIGN_REQUEST) {
            if (resultCode == RESULT_OK) {
                Log.v("MainActivity", "resultOk");
                //только что был закрыт макет, авторизации с помощью гугла
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    //авторизация с помощью гугл успешна
                    GoogleSignInAccount account = task.getResult(ApiException.class);

                    signInWithGoogle(account);
                } catch (ApiException e) {
                    //TODO авторизация с помощью гугла не успешна
                    Log.v("MainActivity", "onError");
                    e.printStackTrace();
                }
            } else {
                if (events != null)
                    events.onAuthCanceled();
            }

        }
    }

    public boolean isUserAuthed() {
        return getFirebaseUser() != null;
    }

    private FirebaseUser getFirebaseUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut();
    }

    /**
     * возвращает смещение по часовому поясу
     *
     * @return
     */
    public static String timeZone() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        String timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
        return timeZone.substring(0, 3) + ":" + timeZone.substring(3, 5);
    }

}
