package com.ignas.android.groceryshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
import com.ignas.android.groceryshoppingapp.View.Layer.CurListViewModel;
import com.ignas.android.groceryshoppingapp.View.Layer.TabAdapter;
import com.ignas.android.groceryshoppingapp.View.Layer.ViewModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "log";

        TabLayout tabLayout;
        TabAdapter tabAdapter;
        ViewPager viewPager;
        TabItem manageItems;
        Toolbar toolbar;

        private ViewModel viewModel;
        private CurListViewModel curListViewModel;

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

        curListViewModel = ViewModelProviders.of(this).get(CurListViewModel.class);

        viewModel = ViewModelProviders.of(this).get(ViewModel.class);
        viewModel.getLiveLists().observe(this, new Observer<ArrayList<ItemList>>() {
            @Override
            public void onChanged(ArrayList<ItemList> lists) {
                if(curListViewModel.getDeleteList() !=null){
                    delDrawerList(curListViewModel.getDeleteList());
                }else{
                    addtoDrawer(lists);
                }
            }
        });

        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        if(previousMenuItem[0] !=null){
                            previousMenuItem[0].setChecked(false);

                            if(previousMenuItem[0].getItemId() == menuItem.getItemId()){
                                menuItem.setChecked(false);
                                curListViewModel.setCurrentList(null);
                                previousMenuItem[0]=null;
                            }else{
                                menuItem.setChecked(true);
                                int id = menuItem.getItemId();
                                ItemList curList = viewModel.findList(id);
                                curListViewModel.setCurrentList(curList);
                                viewPager.setCurrentItem(1);
                                previousMenuItem[0] = menuItem;
                            }
                        }else{
                            Log.d(TAG, "onNavigationItemSelected: selected");
                            menuItem.setChecked(true);
                            int id = menuItem.getItemId();
                            ItemList curList = viewModel.findList(id);
                            curListViewModel.setCurrentList(curList);
                            viewPager.setCurrentItem(1);
                            previousMenuItem[0] = menuItem;
                        }
                        drawerLayout.closeDrawers();
                        return true;
                    }
                });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    Log.d(TAG, "onTabUnselected: list");
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition()==1){
                    //LayoutInflater inflater = getLayoutInflater();

                    //View view =  inflater.inflate(R.layout.fragment_manage_lists, null, false);
                    //RecyclerView rec = view.findViewById(R.id.test_list);
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

    }
    public void delDrawerList(ItemList list){
        menu = mNavigationView.getMenu();
        if(menu.hasVisibleItems()){
            menu.removeItem(list.getList_Id());
            previousMenuItem[0]=null;
            curListViewModel.deleted();
        }
        refreshDrawer();

    }
    public void addtoDrawer(ArrayList<ItemList>lists){
        menu = mNavigationView.getMenu();
        if(!menu.hasVisibleItems()){
            for(ItemList list:lists){
                menu.add(Menu.NONE,list.getList_Id(),Menu.FLAG_PERFORM_NO_CLOSE,list.getListName()+" "+list.getShopName());
               // menu.add(list.getListName()+" "+list.getShopName());
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
        viewModel.refresh_Db_Lists();
        Item  itemToBeScheduled = viewModel.refresh_Db_Items();
        if(itemToBeScheduled != null){
            Intent intent = new Intent(this, Alarm.class);
            intent.putExtra("name",itemToBeScheduled.getItemName());
            intent.putExtra("time",itemToBeScheduled.getRunOutDate().getTime());
            startService(intent);
            Log.i("log", "re_scheduleAlarm: "+itemToBeScheduled.getItemName());
        }

    }
    @Override
    protected void onRestart() {
        super.onRestart();
        viewModel.addItem(new Item());
    }
}
