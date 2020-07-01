package com.app.audiobook.ux;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.app.audiobook.R;
import com.app.audiobook.audio.AudioLibraryManager;
import com.app.audiobook.audio.catalog.ShopCatalog;
import com.app.audiobook.audio.catalog.UserCatalog;
import com.app.audiobook.audio.service.BackgroundSoundService;
import com.app.audiobook.audio.book.AudioBook;
import com.app.audiobook.audio.service.player.PlayerAdapter;
import com.app.audiobook.audio.timer.StopPlayerTimer;
import com.app.audiobook.component.JSONManager;
import com.app.audiobook.fragment.PurchaseFragment;
import com.app.audiobook.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MainActivity extends BaseActivity {

    public static final String URL = "https://firebasestorage.googleapis.com/v0/b/bookaudio-66877.appspot.com/o/Роулинг%20Джоан%20Кэтлин%20-%20Сказки%20барда%20Бидля%2FПредисловие.mp3?alt=media&token=e9778a39-a0d7-4e3d-820e-24744444c1f7";
    public UserCatalog userCatalog;
    public ShopCatalog shopCatalog;
    public PlayerAdapter playerAdapter;

    public StopPlayerTimer stopPlayerTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    ViewPager viewPager;
    SectionsPagerAdapter sectionsPagerAdapter;

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
                        alpha = 0.3f;
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

        viewPager.setCurrentItem(2);

    }

    public void initPurchaseFragment(AudioBook audioBook){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        PurchaseFragment fragment = new PurchaseFragment(audioBook);

        fragmentTransaction.addToBackStack("PurchaseFragment");
        fragmentTransaction.add(R.id.frame_layout, fragment).commit();
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

}