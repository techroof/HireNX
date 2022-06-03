package com.app.hirenx.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.hirenx.LayoutsPartnerProfile.MenuFragment;
import com.app.hirenx.LayoutsPartnerProfile.ProfileFragment;

public class LayoutPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 2;

    public LayoutPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public LayoutPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:

                return new ProfileFragment();

            case 1:
                return new MenuFragment();

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}

