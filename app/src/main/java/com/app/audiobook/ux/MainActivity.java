package com.app.audiobook.ux;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;

import com.app.audiobook.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.audiobook.ui.main.SectionsPagerAdapter;

public class MainActivity extends BaseActivity {

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViewPager();
        initTabs();
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
}