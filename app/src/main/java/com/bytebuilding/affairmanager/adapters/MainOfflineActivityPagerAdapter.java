package com.bytebuilding.affairmanager.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bytebuilding.affairmanager.fragments.CurrentOfflineAffairsFragment;
import com.bytebuilding.affairmanager.fragments.DoneOfflineAffairsFragment;

public class MainOfflineActivityPagerAdapter extends FragmentStatePagerAdapter {

    private byte numOfTabs;

    public static final int CURRENT_AFFAIR_FRAGMENT_POSITION = 0;
    public static final int DONE_AFFAIR_FRAGMENT_POSITION = 1;

    private CurrentOfflineAffairsFragment currentOfflineAffairsFragment;
    private DoneOfflineAffairsFragment doneOfflineAffairsFragment;

    public MainOfflineActivityPagerAdapter(FragmentManager fm, byte numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;

        currentOfflineAffairsFragment = new CurrentOfflineAffairsFragment();
        doneOfflineAffairsFragment = new DoneOfflineAffairsFragment();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return this.currentOfflineAffairsFragment;
            case 1:
                return this.doneOfflineAffairsFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
