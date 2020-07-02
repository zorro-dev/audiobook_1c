package com.app.audiobook.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.audiobook.R;
import com.app.audiobook.fragment.AudioLibraryFragment;
import com.app.audiobook.fragment.CategoryFragment;
import com.app.audiobook.fragment.PlayerFragment;
import com.app.audiobook.fragment.SettingFragment;
import com.app.audiobook.fragment.ShopFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5};
    private static final int[] TAB_ICON = new int[]{R.drawable.ic_nav_shop, R.drawable.ic_nav_audio_library, R.drawable.ic_nav_player, R.drawable.ic_nav_category, R.drawable.ic_nav_setting};

    private final Context mContext;

    private ShopFragment shopFragment;
    private AudioLibraryFragment audioLibraryFragment;
    private PlayerFragment playerFragment;
    private CategoryFragment categoryFragment;
    private SettingFragment settingFragment;

    public ShopFragment getShopFragment() {
        return shopFragment;
    }

    public AudioLibraryFragment getAudioLibraryFragment() {
        return audioLibraryFragment;
    }

    public PlayerFragment getPlayerFragment() {
        return playerFragment;
    }

    public CategoryFragment getCategoryFragment() {
        return categoryFragment;
    }

    public SettingFragment getSettingFragment() {
        return settingFragment;
    }

    private Fragment fragments[] = new Fragment[]{
            shopFragment,
            audioLibraryFragment,
            playerFragment,
            categoryFragment,
            settingFragment
    };

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        fragments[0] = shopFragment = new ShopFragment();
        fragments[1] = audioLibraryFragment = new AudioLibraryFragment();
        fragments[2] = playerFragment = new PlayerFragment();
        fragments[3] = categoryFragment = new CategoryFragment();
        fragments[4] = settingFragment = new SettingFragment();
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragments[position];
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);

        TextView text = v.findViewById(R.id.text);
        text.setText(TAB_TITLES[position]);
        ImageView image = v.findViewById(R.id.image);
        image.setImageResource(TAB_ICON[position]);

        return v;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return fragments.length;
    }
}