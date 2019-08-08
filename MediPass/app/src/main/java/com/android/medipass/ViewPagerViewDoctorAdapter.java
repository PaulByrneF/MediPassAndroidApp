package com.android.medipass;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerViewDoctorAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private String phNo, email, regNo, cStatus;

    public ViewPagerViewDoctorAdapter(FragmentManager fm, int numOfTabs, String phNo, String email, String regNo) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.phNo = phNo;
        this.email = email;
        this.regNo = regNo;


    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                FragmentDoctorInfo fragmentDoctorInfo= new FragmentDoctorInfo();
                Bundle bundle = new Bundle();
                bundle.putString("phNo", phNo);
                bundle.putString("email", email);
                bundle.putString("regNo", regNo);
                fragmentDoctorInfo.setArguments(bundle);
                return fragmentDoctorInfo;

            case 1:
                return new FragmentPracticeinfo();

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
