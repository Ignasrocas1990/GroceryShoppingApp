package com.ignas.android.groceryshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.Alarm;
import com.ignas.android.groceryshoppingapp.View.Layer.Item.AssoViewModel;
import com.ignas.android.groceryshoppingapp.View.Layer.Lists.ListsViewModel;
import com.ignas.android.groceryshoppingapp.View.Layer.TabAdapter;
import com.ignas.android.groceryshoppingapp.View.Layer.Item.ItemViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "log";
    private static final int MANAGE_LISTS_TAB = 1;
    private static final int DESELECT = -1;

        TabLayout tabLayout;
        TabAdapter tabAdapter;
        ViewPager viewPager;
        TabItem manageItems;
        Toolbar toolbar;

        private ItemViewModel itemViewModel;
        private ListsViewModel listsViewModel;
        private AssoViewModel assoViewModel;
        NavigationView mNavigationView;
        DrawerLayout drawerLayout;
        Menu menu;
        final MenuItem[] previousMenuItem = {null};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);
        manageItems = findViewById(R.id.ItemFragment);
        viewPager = findViewById(R.id.pageView);
        mNavigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);

        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        tabAdapter = new TabAdapter(fragmentManager,tabLayout.getTabCount());
        viewPager.setAdapter(tabAdapter);

        listsViewModel = ViewModelProviders.of(this).get(ListsViewModel.class);
        assoViewModel = ViewModelProviders.of(this).get(AssoViewModel.class);
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        Observers();
//drawer on select
        mNavigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    if(previousMenuItem[0] != null){
                        previousMenuItem[0].setChecked(false);

                        if(previousMenuItem[0].getItemId() == menuItem.getItemId()){
                            menuItem.setChecked(false);
                            listsViewModel.setCurrentList(null);
                            previousMenuItem[0]=null;
                        }else{
                            menuItem.setChecked(true);
                            int id = menuItem.getItemId();
                            ItemList curList = listsViewModel.findList(id);
                            listsViewModel.setCurrentList(curList);
                            viewPager.setCurrentItem(MANAGE_LISTS_TAB);

                            previousMenuItem[0] = menuItem;
                        }
                    }else{
                        menuItem.setChecked(true);
                        int id = menuItem.getItemId();
                        ItemList curList = listsViewModel.findList(id);
                        listsViewModel.setCurrentList(curList);
                        viewPager.setCurrentItem(MANAGE_LISTS_TAB);
                        previousMenuItem[0] = menuItem;
                    }
                    drawerLayout.closeDrawers();
                    return true;
                });
//tabs on click
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

    }
    private void Observers(){
        listsViewModel.getLiveLists().observe(this, lists -> {//deletion of current list

            if(listsViewModel.getDeleteList() !=null){
                delDrawerList(listsViewModel.getDeleteList());
            }else{
                addtoDrawer(lists);
            }
        });
// select current list after creation
        listsViewModel.getLiveList().observe(this,curList->{
            //synchronizes current list <=> its associations to items
            if(curList==null){
                assoViewModel.setAsso(DESELECT);
            }else{
                assoViewModel.setAsso(curList.getList_Id());
            }
            if(previousMenuItem[0] == null){
                MenuItem menuItem =menu.findItem(curList.getList_Id());
                menuItem.setChecked(true);
                previousMenuItem[0] = menuItem;
            }

        });
        //TODO continue here (make sure current asso connected to foundASSO & create observer  list<=>asso)

    }
    public void delDrawerList(ItemList list){
        menu = mNavigationView.getMenu();
        if(menu.hasVisibleItems()){
            menu.removeItem(list.getList_Id());
            previousMenuItem[0]=null;
            listsViewModel.deleted();
        }
        refreshDrawer();

    }
    public void addtoDrawer(ArrayList<ItemList>lists){
        menu = mNavigationView.getMenu();
        if(!menu.hasVisibleItems()){
            for(ItemList list:lists){
                menu.add(Menu.NONE,list.getList_Id(),Menu.FLAG_PERFORM_NO_CLOSE,list.getListName()+" "+list.getShopName());
            }
        }else{
            ItemList list = lists.get(lists.size()-1);
            menu.add(Menu.NONE,list.getList_Id(),Menu.FLAG_PERFORM_NO_CLOSE,list.getListName()+" "+list.getShopName());
        }
        refreshDrawer();
    }
    //not my code -----------------
    public void refreshDrawer(){
        for (int i = 0, count = mNavigationView.getChildCount(); i < count; i++) {
            final View child = mNavigationView.getChildAt(i);
            if (child != null && child instanceof ListView) {
                final ListView menuView = (ListView) child;
                final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                wrapped.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Item  itemToBeScheduled = itemViewModel.refresh_Db_Items();

        listsViewModel.refresh_Db_Lists();
        if(itemToBeScheduled != null){
            Intent intent = new Intent(this, Alarm.class);
            intent.putExtra("name",itemToBeScheduled.getItemName());
            intent.putExtra("time",itemToBeScheduled.getRunOutDate().getTime());
            startService(intent);
        }

    }
}
