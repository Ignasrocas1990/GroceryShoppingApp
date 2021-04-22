package com.ignas.android.groceryshoppingapp.View.Layer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    //dbHelper data;
    ViewModel viewModel;
    private int numberOfTabs;
    public TabAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm, tabCount);
        numberOfTabs = tabCount;

    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment = ItemFragment.newInstance();
                break;
            case 1:
                fragment = ListsFragment.newInstance();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

}
