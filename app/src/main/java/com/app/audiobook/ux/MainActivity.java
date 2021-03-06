package com.app.audiobook.ux;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager.widget.ViewPager;

import com.app.audiobook.R;
import com.app.audiobook.audio.AudioLibraryManager;
import com.app.audiobook.utils.PreferenceUtil;
import com.app.audiobook.audio.catalog.ShopCatalog;
import com.app.audiobook.audio.catalog.UserCatalog;
import com.app.audiobook.audio.service.BackgroundSoundService;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.service.player.PlayerAdapter;
import com.app.audiobook.audio.timer.StopPlayerTimer;
import com.app.audiobook.component.JSONManager;
import com.app.audiobook.fragment.CategoryListFragment;
import com.app.audiobook.fragment.DescriptionFragment;
import com.app.audiobook.fragment.DownloadBookFragment;
import com.app.audiobook.fragment.BookFragment;
import com.app.audiobook.ui.main.SectionsPagerAdapter;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    public static final int BOOK_ACTIVITY_REQUEST = 1023;

    public UserCatalog userCatalog;
    public ShopCatalog shopCatalog;
    public PlayerAdapter playerAdapter;

    public StopPlayerTimer stopPlayerTimer;

    public MutableLiveData<AudioBook> currentBook = new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fresco.initialize(this);

        initViewPager();
        initTabs();

        userCatalog = new UserCatalog(getAuthManager().getUser().getId());
        userCatalog.load();

        shopCatalog = new ShopCatalog();
        shopCatalog.load();

        //loadToDatabase();

        playerAdapter = new PlayerAdapter(this);
        //playerAdapter.init();
        //playerAdapter.setAudio(URL);

        stopPlayerTimer = new StopPlayerTimer();
        stopPlayerTimer.setTimerCallbacks(new StopPlayerTimer.TimerCallbacks() {
            @Override
            public void onTimerStarted() {

            }

            @Override
            public void onTimerFinished() {
                stopService(new Intent(MainActivity.this, BackgroundSoundService.class));
                sectionsPagerAdapter.getSettingFragment().onTimerFinished();
            }
        });

    }

    public ViewPager viewPager;
    public SectionsPagerAdapter sectionsPagerAdapter;

    private void initViewPager(){
        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout tabLayout = findViewById(R.id.tabs);

                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    View v = tab.getCustomView();

                    TextView text = v.findViewById(R.id.text);
                    ImageView image = v.findViewById(R.id.image);

                    float alpha = 1f;

                    if (i != position) {
                        alpha = 0.8f;
                        text.setTypeface(Typeface.DEFAULT);
                    } else {
                        alpha = 1f;
                        text.setTypeface(Typeface.DEFAULT_BOLD);
                    }

                    text.setAlpha(alpha);
                    image.setAlpha(alpha);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initTabs(){
        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(sectionsPagerAdapter.getTabView(i));
        }

        final int[] tabLayoutIds = new int[] {
                R.id.tab_1,
                R.id.tab_2,
                R.id.tab_3,
                R.id.tab_4,
                R.id.tab_5,
        };

        for (int i = 0; i < tabLayoutIds.length; i ++) {
            View view = findViewById(tabLayoutIds[i]);

            int finalI = i;
            view.setOnClickListener(v1 -> {
                viewPager.setCurrentItem(finalI);
            });
        }

        viewPager.setCurrentItem(PreferenceUtil.getStartPage(this));

    }

    public void initPurchaseFragment(AudioBook audioBook){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        BookFragment fragment = new BookFragment(audioBook, userCatalog, shopCatalog);

        fragmentTransaction.addToBackStack("PurchaseFragment");
        fragmentTransaction.add(R.id.frame_layout, fragment).commit();
    }

    public void initDescriptionFragment(AudioBook audioBook){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        DescriptionFragment fragment = new DescriptionFragment(audioBook);

        fragmentTransaction.addToBackStack("DescriptionFragment");
        fragmentTransaction.add(R.id.frame_layout, fragment).commit();
    }

    public void initCategoryListFragment(String categoryId){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CategoryListFragment fragment = new CategoryListFragment(categoryId);

        fragmentTransaction.addToBackStack("CategoryListFragment");
        fragmentTransaction.add(R.id.frame_layout, fragment).commit();
    }

    public void initDownloadFragment(AudioBook audioBook) {
        DownloadBookFragment fragment = new DownloadBookFragment(audioBook);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.addToBackStack("DownloadBookFragment");

        transaction.add(R.id.frame_layout, fragment).commit();
    }

    private void loadToDatabase() {
        ArrayList<AudioBook> audioBooks = new ArrayList<>(AudioLibraryManager.getBookList(this));

        // использовалось для загрузки на серв
        for (int i = 0; i < audioBooks.size(); i ++) {
            AudioBook audioBook = audioBooks.get(i);

            String json = JSONManager.exportToJSON(audioBook);
            FirebaseDatabase.getInstance().getReference("BookCatalog")
                    .child("Book").child(audioBook.getId()).setValue(json);
        }
    }


    public void initCurrentAudioBook() {
        String currendBookId = PreferenceUtil.getCurrentAudioBookId(this);

        for (int i = 0; i < userCatalog.getCatalogList().size(); i ++) {
            String bookId = userCatalog.getCatalogList().get(i).getId();

            if (bookId.equals(currendBookId)) {
                currentBook.setValue(userCatalog.getCatalogList().get(i));
            }

            Log.v("lol", "initCurrentAudioBook " + currentBook.getValue().getTitle());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.v("lol", "onActivityResult");

        if (requestCode == BOOK_ACTIVITY_REQUEST) {
            Log.v("lol", "onActivityResult1");
            if (resultCode == RESULT_OK) {
                Log.v("lol", "onActivityResult1");
                String json = data.getStringExtra("audiobook");
                AudioBook audioBook = JSONManager.importFromJSON(json, AudioBook.class);
                currentBook.setValue(audioBook);
                PreferenceUtil.setCurrentAudioBookId(this, audioBook.getId());

                viewPager.setCurrentItem(2);

                Log.v("lol", "onActivityResult " + currentBook.getValue().getTitle());
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}