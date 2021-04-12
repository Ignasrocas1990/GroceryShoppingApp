package com.ignas.android.groceryshoppingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.savedstate.SavedStateRegistry;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.ignas.android.groceryshoppingapp.Logic.PageViewModel;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.DbConnect;
import com.ignas.android.groceryshoppingapp.Service.RealmDb;
import com.ignas.android.groceryshoppingapp.View.Layer.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity {
        BottomNavigationView bottomNav;
        NavController controller;
        PageViewModel viewModel;

        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         bottomNav = findViewById(R.id.toolBar);
         controller = Navigation.findNavController(this,R.id.hostFragment);
         /*
        AppBarConfiguration toolBarConfig = new AppBarConfiguration.Builder(
                R.id.navigation_addItems,R.id.navigation_blankFragment)
                .build();
                NavigationUI.setupActionBarWithNavController(this,controller,toolBarConfig);
 */
                NavigationUI.setupWithNavController(bottomNav,controller);



        }
    public void process_data(View view) {

        int id = bottomNav.getSelectedItemId();
        if(id == R.id.navigation_addItems){

            Toast.makeText(this, "Its Save items screen", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "not working", Toast.LENGTH_SHORT).show();
        }

    }
}
