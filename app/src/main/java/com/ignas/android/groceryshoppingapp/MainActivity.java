package com.ignas.android.groceryshoppingapp;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.ignas.android.groceryshoppingapp.Logic.PageViewModel;
import com.ignas.android.groceryshoppingapp.View.Layer.TabAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "log";
    //BottomNavigationView bottomNav;
        //NavController controller;

        TabLayout tabLayout;
        TabAdapter tabAdapter;
        ViewPager viewPager;
        TabItem manageItems;

        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = findViewById(R.id.tabs);
        manageItems = findViewById(R.id.ItemFragment);
        viewPager = findViewById(R.id.pageView);
        tabAdapter = new TabAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(tabAdapter);
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

            }
        });


/*
        bottomNav = findViewById(R.id.toolBar);
        controller = Navigation.findNavController(this,R.id.hostFragment);

        AppBarConfiguration toolBarConfig = new AppBarConfiguration.Builder(
                R.id.navigation_addItems,R.id.navigation_blankFragment)
                .build();
                NavigationUI.setupActionBarWithNavController(this,controller,toolBarConfig);

            NavigationUI.setupWithNavController(bottomNav,controller);


 */
        }
}
