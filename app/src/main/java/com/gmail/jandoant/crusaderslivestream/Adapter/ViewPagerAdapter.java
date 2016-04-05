package com.gmail.jandoant.crusaderslivestream.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

//ViewPageAdapter
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    //Liste der Fragmente
    private final List<Fragment> mFragmentList = new ArrayList<>();
    //Liste der TabTitel
    private final List<String> mFragmentTitleList = new ArrayList<>();

    //KONSTRUKTOR
    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }


    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    //Fragment zu Liste hinzufügen
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    public void removeFragment(Fragment fragment, String title) {
        mFragmentList.remove(fragment);
        mFragmentTitleList.remove(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
