package com.android.medipass;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter  extends FragmentPagerAdapter {

    private int numOfTabs;

    public ViewPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);

        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new AllDoctorsFragment();

            case 1:
                return new MyDoctorsFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
