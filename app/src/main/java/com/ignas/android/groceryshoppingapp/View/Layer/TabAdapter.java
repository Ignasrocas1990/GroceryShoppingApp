package com.ignas.android.groceryshoppingapp.View.Layer;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.util.ArrayList;

public class TabAdapter extends FragmentPagerAdapter {
    //dbHelper data;
    ViewModel viewModel;
    private int numberOfTabs;
    ArrayList<Item> items;
    ArrayList<ItemList> itemLists;
    public TabAdapter(@NonNull FragmentManager fm, int tabCount) {
        super(fm, tabCount);
        numberOfTabs = tabCount;

    }
    public void updateViewItems(ArrayList<Item> items){
        this.items = items;
    }
    public void updateViewLists(ArrayList<ItemList> lists){
        this.itemLists = lists;

    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        switch (position){
            case 0:
                fragment = ItemFragment.newInstance(items);
                break;
            case 1:
                fragment = ManageListsFragment.newInstance(itemLists,items);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

}
