package com.app.hirenx.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.app.hirenx.LayoutsConsumerProfile.HomeFragment;
import com.app.hirenx.LayoutsConsumerProfile.MenuClientFragment;
import com.app.hirenx.LayoutsConsumerProfile.ProfileClientFragment;
import com.app.hirenx.LayoutsPartnerProfile.MenuFragment;
import com.app.hirenx.LayoutsPartnerProfile.ProfileFragment;

public class ClientLayoutPagerAdapter extends FragmentPagerAdapter {

    final int PAGE_COUNT = 3;

    public ClientLayoutPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ClientLayoutPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {

            case 0:

                return new HomeFragment();

            case 1:
                return new ProfileClientFragment();

            case 2:
                return new MenuClientFragment();

            default:
                return new HomeFragment();
        }

    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}

