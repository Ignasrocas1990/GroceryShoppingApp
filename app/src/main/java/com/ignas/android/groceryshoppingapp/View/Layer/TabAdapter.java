package com.ignas.android.groceryshoppingapp.View.Layer;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ignas.android.groceryshoppingapp.BlankFragment;
import com.ignas.android.groceryshoppingapp.Logic.PageViewModel;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;

import java.util.ArrayList;

public class TabAdapter extends FragmentPagerAdapter {
    PageViewModel data;
    private int numberOfTabs;
    ArrayList<Item> list;
    public TabAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm, tabCount);
        numberOfTabs = tabCount;
        data = PageViewModel.getInstance();
        list = data.getItems();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Fragment fragment=null;
        switch (position){
            case 0:

                fragment = ItemFragment.newInstance(list);
                break;
            case 1:
                fragment = BlankFragment.newInstance("blank","or new fragment");
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

}
