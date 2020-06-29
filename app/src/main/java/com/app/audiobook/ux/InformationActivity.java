package com.app.audiobook.ux;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.app.audiobook.R;
import com.app.audiobook.ux.auth.AuthActivity;
import com.app.audiobook.ux.information.ui.information.InformationPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class InformationActivity extends AppCompatActivity {

    ViewPager viewPager;
    InformationPagerAdapter informationPagerAdapter;

    private int[] colors = new int[] {
            R.color.colorCategory1,
            R.color.colorCategory2,
            R.color.colorCategory3,
            R.color.colorCategory4,
            R.color.colorCategory5,
            R.color.colorCategory6,
            R.color.colorCategory7,
            R.color.colorCategory8,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);


        initViewPager();
        initTabs();

    }

    private void initViewPager() {
        informationPagerAdapter = new InformationPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pagerInformation);
        viewPager.setAdapter(informationPagerAdapter);

        ConstraintLayout alpha = findViewById(R.id.alpha);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                TabLayout tabLayout = findViewById(R.id.tabsInformation);

                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    TabLayout.Tab tab = tabLayout.getTabAt(i);
                    View v = tab.getCustomView();

                    ImageView image = v.findViewById(R.id.image);

                    //image.setColorFilter(getResources().getColor(colors[i -  (i / colors.length) * colors.length ]));

                    float alpha = 1f;

                    if (i != position) {
                        alpha = 0.3f;
                        image.setScaleX(0.9f);
                        image.setScaleY(0.9f);
                    } else {
                        alpha = 1f;
                        image.setScaleX(1.2f);
                        image.setScaleY(1.2f);
                    }



                    image.setAlpha(alpha);
                }

                if(position == 2){
                    initProgressBar();
                    alpha.setVisibility(View.VISIBLE);
                    Toast.makeText(InformationActivity.this, "Переход на активность регистрации через 5 секунд", Toast.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(getBaseContext(), AuthActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 5000);

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    int progress = 0;
    ProgressBar progressBar;

    private void initProgressBar(){
        progressBar = findViewById(R.id.progressBarHorizontal);
        progressBar.setVisibility(View.VISIBLE);
        new Thread(runnable).start();
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            while (progress < 500) {
                try {
                    myHandler.sendMessage(myHandler.obtainMessage());
                    Thread.sleep(10);
                } catch (Throwable t) {
                }
            }
        }

        @SuppressLint("HandlerLeak")
        Handler myHandler = new Handler() {
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(Message msg) {
                progress++;
                progressBar.setProgress(progress);
            }
        };
    };

    private void initTabs() {
        TabLayout tabLayout = findViewById(R.id.tabsInformation);
        tabLayout.setupWithViewPager(viewPager);
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(informationPagerAdapter.getTabView(i));
        }

        viewPager.setCurrentItem(1);
        viewPager.setCurrentItem(0);
    }
}