package com.ignas.android.groceryshoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.ignas.android.groceryshoppingapp.Logic.dbHelper;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Service.Alarm;
import com.ignas.android.groceryshoppingapp.View.Layer.TabAdapter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "log";

        TabLayout tabLayout;
        TabAdapter tabAdapter;
        ViewPager viewPager;
        TabItem manageItems;
        Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        }

    @Override
    protected void onStop() {
        super.onStop();

       Item  itemToBeScheduled = dbHelper.getInstance().update();
        if(itemToBeScheduled != null){
            Intent intent = new Intent(this, Alarm.class);
            intent.putExtra("name",itemToBeScheduled.getItemName());
            intent.putExtra("time",itemToBeScheduled.getRunOutDate().getTime());
            this.startService(intent);
            Log.i("log", "re_scheduleAlarm: "+itemToBeScheduled.getItemName());

        }
            Log.d(TAG, "onStop: come back");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dbHelper.getInstance().addEmpty();
    }
}
