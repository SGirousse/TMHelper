package com.paugo.tmhelper.activity.options.viewer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.paugo.tmhelper.R;
import com.paugo.tmhelper.activity.options.fragment.GameOptionsExtensionSelectionFragment;
import com.paugo.tmhelper.activity.options.fragment.GameOptionsPlayersSelectionFragment;
import com.paugo.tmhelper.activity.options.fragment.GameOptionsTimingsSelectionFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.game_options_selection_tab_1_title, R.string.game_options_selection_tab_2_title, R.string.game_options_selection_tab_3_title};
    private static int NUM_TABS = 3;
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return GameOptionsPlayersSelectionFragment.newInstance(0, "Page # 1");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return GameOptionsTimingsSelectionFragment.newInstance(1, "Page # 2");
            case 2: // Fragment # 1 - This will show SecondFragment
                return GameOptionsExtensionSelectionFragment.newInstance(2, "Page # 3");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }
}