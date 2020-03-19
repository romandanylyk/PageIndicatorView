package com.rd.pageindicatorview.viewpager2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class SamplePagerAdapter extends FragmentStateAdapter {

    private static final int NUM_PAGES = 5;

    SamplePagerAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return new SamplePageFragment();
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}