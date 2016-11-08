package com.bytebuilding.affairmanager.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bytebuilding.affairmanager.fragments.CurrentOfflineAffairsFragment;
import com.bytebuilding.affairmanager.fragments.DoneOfflineAffairsFragment;

public class MainOfflineActivityPagerAdapter extends FragmentStatePagerAdapter {

    private byte numOfTabs;

    public MainOfflineActivityPagerAdapter(FragmentManager fm, byte numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new CurrentOfflineAffairsFragment();
            case 1:
                return new DoneOfflineAffairsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
