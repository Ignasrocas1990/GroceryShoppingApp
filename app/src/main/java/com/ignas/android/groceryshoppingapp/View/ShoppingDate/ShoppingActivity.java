package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.Service.AlarmService;
import com.ignas.android.groceryshoppingapp.View.Item.ItemViewModel;
import com.ignas.android.groceryshoppingapp.View.Lists.AssoViewModel;
import com.ignas.android.groceryshoppingapp.View.Lists.ListsViewModel;

import java.util.ArrayList;

public class ShoppingActivity extends AppCompatActivity {
        ItemViewModel itemViewModel;
        ListsViewModel listViewModel;
        AssoViewModel assoViewModel;
        RecyclerView recyclerView;
        ShoppingRecyclerAdapter adapter;
        EditText newName,newAmount,newPrice;
        TextView total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        recyclerView = findViewById(R.id.shopping_Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        newName = findViewById(R.id.newName);
        newAmount = findViewById(R.id.newAmount);
        newPrice = findViewById(R.id.newPrice);
        total = findViewById(R.id.total);

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);
        listViewModel = ViewModelProviders.of(this).get(ListsViewModel.class);
        assoViewModel = ViewModelProviders.of(this).get(AssoViewModel.class);
        cancelAlarm();
        itemViewModel.setShoppingDate();
        ArrayList<Item> items = itemViewModel.createShoppingItems();
        ArrayList<Association> displayAssos = assoViewModel.findAssociations(items);
        ArrayList<ItemList> lists =  listViewModel.findLists_forItem(displayAssos);



        adapter = new ShoppingRecyclerAdapter(items,displayAssos,lists,new ShoppingRecyclerAdapter.onItemClickListner() {
            @Override
            public void onItemClick(Item item) {

            }
        });
        recyclerView.setAdapter(adapter);
        //observers();
    }
//TODO observe associations, and create association
/*
    public void observers(){
        itemViewModel.getLiveItems().observe(this, new Observer<ArrayList<Item>>() {
            @Override
            public void onChanged(ArrayList<Item> items) {
                adapter.setItems(items);
                adapter.notifyDataSetChanged();
            }
        });
    }

 */




    public void save(View view) {
    }

    public void addNew(View view) {

    }
    @Override
    protected void onStop() {
        super.onStop();
        itemViewModel.reSyncItems();
        itemViewModel.updateDbItems();
        //Item scheduledItem = itemViewModel.getScheduledItem();
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra("flag",1);
        startService(intent);

/*
        if( scheduledItem != null){
            int ntfType = 0;
            Intent intent = new Intent(this, AlarmService.class);
            intent.putExtra("name",scheduledItem.getItemName());
            intent.putExtra("time",scheduledItem.getRunOutDate().getTime());
            intent.putExtra("flag",0);
            intent.putExtra("type",ntfType);
            startService(intent);
        }
 */
        /*
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra("name","restart");
        intent.putExtra("flag",1);
        startService(intent);
         */
    }
    public void cancelAlarm(){
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra("name","");
        intent.putExtra("flag",-1);
        startService(intent);
    }
}