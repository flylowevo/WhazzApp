package com.dajo.whazzapp.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dajo.whazzapp.ui.FriendsFragment;
import com.dajo.whazzapp.ui.GroupsFragment;
import com.dajo.whazzapp.ui.InboxFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    final int PAGE_COUNT = 3;
    private Context context;

    public ViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InboxFragment();
            case 1:
                return new FriendsFragment();
            case 2:
                return new GroupsFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "Inbox";
            case 1:
                return "Friends";
            case 2:
                return "Groups";
        }

        return null;
    }
}
