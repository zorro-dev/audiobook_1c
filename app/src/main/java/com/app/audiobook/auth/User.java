package com.app.audiobook.auth;

import com.app.audiobook.database.DatabaseEntity;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class User extends DatabaseEntity {

    private String id;
    private UserValue<String> name;
    private UserValue<String> mail;

    private UserValue<String> registryTime;
    private UserValue<String> lastEnterTime;
    private UserValue<String> GMT;

    public static String TYPE_USER = "user";
    public static String TYPE_ADMIN = "admin";
    private UserValue<String> userType;

    private UserValue<String> authType;

    private UserValue<Integer> avatarIndex;

    private UserValue<String> ownInviteCode;
    private UserValue<String> inviteCode;

    private UserValue<Boolean> isReviewApp;
    private UserValue<Boolean> isNoAds;

    public User(Map<String, Object> entityMap) {
        super(entityMap);

        id = (String) entityMap.get("id");

        initValues();
        setValues(entityMap);
    }


    public User(String id) {
        this.id = id;

       initValues();

       setDefaultValues();
    }

    private void initValues() {
        name = new UserValue<>("name", this);
        mail = new UserValue<>("mail", this);
        registryTime = new UserValue<>("registryTime", this);
        lastEnterTime = new UserValue<>("lastEnterTime", this);
        GMT = new UserValue<>("GMT", this);

        userType = new UserValue<>("userType", this);
        authType = new UserValue<>("authType", this);
        avatarIndex = new UserValue<>("avatarIndex", this);

        ownInviteCode = new UserValue<>("ownInviteCode", this);
        inviteCode = new UserValue<>("inviteCode", this);

        isReviewApp = new UserValue<>("isReviewApp", this);
        isNoAds = new UserValue<>("isNoAds", this);
    }

    private void setValues(Map<String, Object> entityMap) {

        id = (String) entityMap.get("id");

        name.setValue((String) entityMap.get("name"));
        mail.setValue((String) entityMap.get("mail"));
        registryTime.setValue((String) entityMap.get("registryTime"));
        lastEnterTime.setValue((String) entityMap.get("lastEnterTime"));
        GMT.setValue((String) entityMap.get("GMT"));

        userType.setValue((String) entityMap.get("userType"));
        authType.setValue((String) entityMap.get("authType"));
        avatarIndex.setValue(((Long) entityMap.get("avatarIndex")).intValue());

        ownInviteCode.setValue((String) entityMap.get("ownInviteCode"));
        inviteCode.setValue((String) entityMap.get("inviteCode"));

        isReviewApp.setValue((Boolean) entityMap.get("isReviewApp"));
        isNoAds.setValue((Boolean) entityMap.get("isNoAds"));
    }

    private void setDefaultValues() {
        name.setValue("");
        mail.setValue("");
        registryTime.setValue("");
        lastEnterTime.setValue("");
        GMT.setValue("");

        userType.setValue(TYPE_USER);
        authType.setValue(AuthManager.AUTH_TYPE_ANONYMOUSLY);
        avatarIndex.setValue(0);

        ownInviteCode.setValue(null);
        inviteCode.setValue(null);

        isReviewApp.setValue(false);
        isNoAds.setValue(false);
    }


    @Override
    public Task syncWithDatabase() {
        return FirebaseDatabase.getInstance().getReference("Users")
                .child(getId()).setValue(getEntityMap());
    }

    @Override
    public Map getEntityMap() {
        Map<String, Object> entityMap = new HashMap<>();

        entityMap.put("id", id);
        entityMap.put("name", name.getValue());
        entityMap.put("mail", mail.getValue());
        entityMap.put("registryTime", registryTime.getValue());
        entityMap.put("lastEnterTime", lastEnterTime.getValue());
        entityMap.put("GMT", GMT.getValue());
        entityMap.put("userType", userType.getValue());
        entityMap.put("authType", authType.getValue());
        entityMap.put("avatarIndex", avatarIndex.getValue());
        entityMap.put("ownInviteCode", ownInviteCode.getValue());
        entityMap.put("inviteCode", inviteCode.getValue());
        entityMap.put("isReviewApp", isReviewApp.getValue());
        entityMap.put("isNoAds", isNoAds.getValue());

        return entityMap;
    }

    public boolean isInviteCodeUsed() {
        return !(getInviteCode().getValue() == null || getInviteCode().getValue() != null && getInviteCode().getValue().length() == 0);
    }

    public boolean isAuthedAnonymously() {
        return getAuthType().getValue().equals(AuthManager.AUTH_TYPE_ANONYMOUSLY);
    }

    public void createOwnInviteCode() {
        DatabaseReference userOwnInviteCodeRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(getId()).child("ownInviteCode");

        String key = userOwnInviteCodeRef.push().getKey();
        getOwnInviteCode().setValue(key);

        DatabaseReference commonInviteCodesRef = FirebaseDatabase.getInstance().getReference("InviteCodes")
                .child("ByUsers").child(getOwnInviteCode().getValue());

        userOwnInviteCodeRef.setValue(getOwnInviteCode().getValue());
        commonInviteCodesRef.setValue(getId());
    }

    public boolean needToCreateOwnInviteCode() {
        return getOwnInviteCode().getValue() == null || getOwnInviteCode().getValue().length() == 0;
    }

    public String getId() {
        return id;
    }

    public UserValue<String> getName() {
        return name;
    }

    public UserValue<String> getMail() {
        return mail;
    }

    public UserValue<String> getRegistryTime() {
        return registryTime;
    }

    public UserValue<String> getLastEnterTime() {
        return lastEnterTime;
    }

    public UserValue<String> getGMT() {
        return GMT;
    }

    public UserValue<String> getUserType() {
        return userType;
    }

    public UserValue<String> getAuthType() {
        return authType;
    }

    public UserValue<Integer> getAvatarIndex() {
        return avatarIndex;
    }

    public UserValue<String> getOwnInviteCode() {
        return ownInviteCode;
    }

    public UserValue<String> getInviteCode() {
        return inviteCode;
    }

    public UserValue<Boolean> getIsReviewApp() {
        return isReviewApp;
    }

    public UserValue<Boolean> getIsNoAds() {
        return isNoAds;
    }
}
