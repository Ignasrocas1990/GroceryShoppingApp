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

import com.ignas.android.groceryshoppingapp.Logic.ShoppingDay;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Models.ShoppingItem;
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
        ShoppingDay shoppingDay;
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

        itemViewModel = new ItemViewModel();
        listViewModel = new ListsViewModel();
        assoViewModel = new AssoViewModel();


        cancelAlarm();
        itemViewModel.setShoppingDate();
        ArrayList<Item> items = itemViewModel.createShoppingItems();
        ArrayList<Association> displayAssos = assoViewModel.findAssociations(items);
        ArrayList<ItemList> lists =  listViewModel.findLists_forItem(displayAssos);

        shoppingDay = new ShoppingDay();
        shoppingDay.createItems(items, displayAssos, lists);


        adapter = new ShoppingRecyclerAdapter(new ShoppingRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemBuy(ShoppingItem item) {
                if(total.getText().toString().equals("total")){
                    shoppingDay.setTotal(item.getPrice());
                }else{
                    shoppingDay.addToTotal(item.getPrice());
                }
            }
            @Override
            public void onCancel(ShoppingItem item) {
                if(total.getText().toString().equals("0.0")){
                    total.setText(R.string.total);
                }else{
                    shoppingDay.subtractTotal(item.getPrice());
                }
            }
        });
        recyclerView.setAdapter(adapter);
        observers();
    }

//all the observers
    public void observers(){
        shoppingDay.getLiveSpList().observe(this, new Observer<ArrayList<ShoppingItem>>() {
            @Override
            public void onChanged(ArrayList<ShoppingItem> shoppingItems) {
                adapter.setItems(shoppingItems);
                adapter.notifyDataSetChanged();
            }
        });
        shoppingDay.getLiveTotal().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                total.setText(String.valueOf(aFloat));
            }
        });

    }

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