package com.ignas.android.groceryshoppingapp;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.AlarmService;
import com.ignas.android.groceryshoppingapp.Logic.ItemViewModel;
import com.ignas.android.groceryshoppingapp.Logic.AssoViewModel;
import com.ignas.android.groceryshoppingapp.Logic.ListsViewModel;
import com.ignas.android.groceryshoppingapp.Logic.DateViewModel;
import com.ignas.android.groceryshoppingapp.View.TabAdapter;

import org.jetbrains.annotations.NonNls;

import java.util.ArrayList;
import java.util.Random;
/***
 * Author:Ignas Rocas
 * Student Id: C00135830
 * Date: 28/05/2021
 * Purpose: Project, Initialize main components
 */
public class MainActivity extends AppCompatActivity {
    private static final int MANAGE_LISTS_TAB = 1;
    private static final int SHOPPING_TAB = 4;
    private static final int DESELECT = -1;
    private final int[] icos = {R.drawable.ic_list_ico1,R.drawable.ic_list_ico2
            ,R.drawable.ic_list_ico3,R.drawable.ic_list_ico4,R.drawable.ic_list_ico5};

        TabLayout tabLayout;
        TabAdapter tabAdapter;
        ViewPager viewPager;
        TabItem manageItems;
        Toolbar toolbar;

        private ItemViewModel itemViewModel;
        private ListsViewModel listViewModel;
        private AssoViewModel assoViewModel;
        private DateViewModel dateViewModel;
        NavigationView mNavigationView;
        DrawerLayout drawerLayout;
        @NonNls
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

        FragmentManager fragmentManager = getSupportFragmentManager();
        tabAdapter = new TabAdapter(fragmentManager,tabLayout.getTabCount());
        viewPager.setAdapter(tabAdapter);


//initialize view Models
        listViewModel = ViewModelProviders.of(this).get(ListsViewModel.class);
        assoViewModel = ViewModelProviders.of(this).get(AssoViewModel.class);
        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        dateViewModel = ViewModelProviders.of(this).get(DateViewModel.class);

        Observers();

//drawer on select
        mNavigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    if(previousMenuItem[0] != null){
                        previousMenuItem[0].setChecked(false);

                        if(previousMenuItem[0].getItemId() == menuItem.getItemId()){
                            menuItem.setChecked(false);
                            listViewModel.setCurrentList(null);
                            previousMenuItem[0]=null;
                        }else{
                            menuItem.setChecked(true);
                            int id = menuItem.getItemId();
                            ItemList curList = listViewModel.findList(id);
                            listViewModel.setCurrentList(curList);
                            viewPager.setCurrentItem(MANAGE_LISTS_TAB);

                            previousMenuItem[0] = menuItem;
                        }
                    }else{
                        menuItem.setChecked(true);
                        int id = menuItem.getItemId();
                        ItemList curList = listViewModel.findList(id);
                        listViewModel.setCurrentList(curList);
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

isItShoppingTime();
cancelAlarms();
}
    //checks if activity opened from notification
    private void isItShoppingTime(){
        Intent intent = getIntent();
        if(intent!=null){
            int openingType = intent.getIntExtra("type",0);
            if (openingType != 0){

                itemViewModel.removeShoppingDate();//sets shopping date to null
                viewPager.setCurrentItem(SHOPPING_TAB);
            }
        }
    }
//all the data observers----------------------(for drawer)
    private void Observers(){
        listViewModel.getLiveLists().observe(this, lists -> {//deletion of current list

            if(listViewModel.getDeleteList() !=null){
                delDrawerList(listViewModel.getDeleteList());
            }else{
                addtoDrawer(lists);
            }
        });

// select current list after creation
        listViewModel.getCurrLiveList().observe(this, curList->{
            //synchronizes current list <=> its associations to items
            @NonNls MenuItem menuItem;
            if(curList==null){
                assoViewModel.setAsso(DESELECT);
            }else{
                assoViewModel.setAsso(curList.getList_Id());

                if(previousMenuItem[0] == null){
                    menuItem =menu.findItem(curList.getList_Id());
                    menuItem.setChecked(true);
                    previousMenuItem[0] = menuItem;
                }else{
                    menuItem = menu.findItem(curList.getList_Id());
                    menuItem.setTitle(curList.getListName()+" "+curList.getShopName());
                }

            }

        });
    }
//remove list to a drawer
    public void delDrawerList(ItemList list){
        menu = mNavigationView.getMenu();
        if(menu.hasVisibleItems()){
            menu.removeItem(list.getList_Id());
            previousMenuItem[0]=null;
            listViewModel.deletedCurrent();
        }
        refreshDrawer();

    }
//add list to a drawer
    public void addtoDrawer(ArrayList<ItemList>lists){
        menu = mNavigationView.getMenu();
        Random r = new Random();
        int randomIndex;
        if(!menu.hasVisibleItems()){
            for(ItemList list:lists){
                randomIndex =  r.nextInt(5-1+1);

                menu.add(Menu.NONE,list.getList_Id(),Menu.FLAG_PERFORM_NO_CLOSE,
                        list.getListName()+" "+list.getShopName()).setIcon(icos[randomIndex]);

            }
        }else{
            randomIndex =  r.nextInt(5-1+1);
            ItemList list = lists.get(lists.size()-1);
            menu.add(Menu.NONE,list.getList_Id(),Menu.FLAG_PERFORM_NO_CLOSE,list.getListName()+" "+list.getShopName()).setIcon(icos[randomIndex]);
        }
        refreshDrawer();
    }
    //not my code -----------------(run through children and get adapter and notifyDataSetChanged)
    public void refreshDrawer(){
        for (int i = 0, count = mNavigationView.getChildCount(); i < count; i++) {
            final View child = mNavigationView.getChildAt(i);
            if (child != null && child instanceof ListView) {
                final ListView menuView = (ListView) child;
                final HeaderViewListAdapter adapter = (HeaderViewListAdapter) menuView.getAdapter();
                final BaseAdapter wrapped = (BaseAdapter) adapter.getWrappedAdapter();
                wrapped.notifyDataSetChanged(); //< important this
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        if(dateViewModel.updateSwitch()){ //checks if switch was off and now its on
            itemViewModel.reSyncItems();
        }

//start alarm service if switch is on.
        if(dateViewModel.getSwitch()){
                Intent intent = new Intent(this, AlarmService.class);
                intent.putExtra("flag",1);
                startService(intent);
        }
    }
    private void cancelAlarms(){
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra("flag",-1);
        intent.putExtra("name","");
        startService(intent);
    }
    //cancels alarm again in case resumed
    @Override
    protected void onPostResume() {
        super.onPostResume();
        cancelAlarms();
    }
}
