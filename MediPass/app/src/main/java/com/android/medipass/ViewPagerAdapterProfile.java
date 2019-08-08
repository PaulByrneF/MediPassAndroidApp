package com.android.medipass;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapterProfile extends FragmentPagerAdapter {


    private int numOfTabs;
    private Profile profile;

    public ViewPagerAdapterProfile(FragmentManager fm, int numOfTabs, Profile profile) {
        super(fm);

        this.numOfTabs = numOfTabs;
        this.profile = profile;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentBasicInfo fragmentBasicInfo= new FragmentBasicInfo();
                Bundle bundle = new Bundle();
                bundle.putString("dob", profile.getDob());
                bundle.putString("sex", profile.getSex());
                bundle.putString("phHome", profile.getPhNo());
                bundle.putString("phMobile", profile.getPhMobNo());
                bundle.putString("smoker", Integer.toString(profile.getSmoker()));
                bundle.putString("drinker", Integer.toString(profile.getDrinker()));
                bundle.putString("height", Integer.toString(profile.getHeight()));
                bundle.putString("weight", Integer.toString(profile.getWeight()));
                bundle.putString("bType", profile.getbType());
                bundle.putString("dnr", Integer.toString(profile.getDnr()));
                fragmentBasicInfo.setArguments(bundle);
                return fragmentBasicInfo;

            case 1:
                return new FragmentMyContacts();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
