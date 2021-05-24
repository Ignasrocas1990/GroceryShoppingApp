package com.ignas.android.groceryshoppingapp.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ignas.android.groceryshoppingapp.View.Item.ItemFragment;
import com.ignas.android.groceryshoppingapp.View.Lists.ListsFragment;
import com.ignas.android.groceryshoppingapp.View.ShoppingDate.DateFragment;
import com.ignas.android.groceryshoppingapp.View.Report.ReportFragment;
import com.ignas.android.groceryshoppingapp.View.ShoppingDate.ShoppingFragment;

import java.util.Objects;

public class TabAdapter extends FragmentPagerAdapter {
    //dbHelper data;
    private final int numberOfTabs;
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
                break;
            case 2:
                fragment = DateFragment.newInstance();
                break;
            case 3:
                fragment = ReportFragment.newInstance();
                break;
            case 4:
                fragment = ShoppingFragment.newInstance();
                break;
        }
        return Objects.requireNonNull(fragment);
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

}
