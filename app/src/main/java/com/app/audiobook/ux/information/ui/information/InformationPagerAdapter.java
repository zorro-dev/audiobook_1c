package com.app.audiobook.ux.information.ui.information;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.audiobook.R;
import com.app.audiobook.ux.information.ui.information.fragment.Fragment1;
import com.app.audiobook.ux.information.ui.information.fragment.Fragment2;
import com.app.audiobook.ux.information.ui.information.fragment.Fragment3;


/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class InformationPagerAdapter extends FragmentPagerAdapter {

    private static final int[] TAB_ICON = new int[]{R.drawable.icon1, R.drawable.icon2, R.drawable.icon3};
    //private static final int[] TAB_ICON = new int[]{R.drawable.ic_round, R.drawable.ic_round, R.drawable.ic_round};

    private final Context mContext;

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;

    private Fragment fragments[] = new Fragment[]{
            fragment1,
            fragment2,
            fragment3
    };

    public InformationPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;

        fragments[0] = fragment1 = new Fragment1();
        fragments[1] = fragment2 = new Fragment2();
        fragments[2] = fragment3 = new Fragment3();

    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return fragments[position];
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(mContext).inflate(R.layout.information_tab, null);

        ImageView image = v.findViewById(R.id.image);
        image.setImageResource(TAB_ICON[position]);

        return v;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return fragments.length;
    }
}