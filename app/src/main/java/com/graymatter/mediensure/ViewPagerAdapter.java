package com.graymatter.mediensure;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.graymatter.mediensure.fragments.NotVerifiedFragment;
import com.graymatter.mediensure.fragments.PendingFragment;
import com.graymatter.mediensure.fragments.VerifiedFragment;

public class ViewPagerAdapter
        extends FragmentPagerAdapter {

    public ViewPagerAdapter(
            @NonNull FragmentManager fm)
    {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position)
    {
        Fragment fragment = null;
        if (position == 0)
            fragment = new PendingFragment();
        else if (position == 1)
            fragment = new NotVerifiedFragment();
        else if (position == 2)
            fragment = new VerifiedFragment();
        return fragment;
    }

    @Override
    public int getCount()
    {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        String title = null;
        if (position == 0)
            title = "Pending";
        else if (position == 1)
            title = "Not Verified";
        else if (position == 2)
            title = "Verified";
        return title;
    }
}
