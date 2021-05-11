package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final int MAX_LENGTH  = 7;
    private static final int MAX_CHARS = 20;

        Context context=null;
        ItemViewModel itemViewModel;
        ListsViewModel listViewModel;
        AssoViewModel assoViewModel;
        DateViewModel dateViewModel;
        RecyclerView recyclerView;
        ShoppingRecyclerAdapter adapter;
        EditText nameEditText,amountEditText,priceEditText;
        TextView totalView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        recyclerView = findViewById(R.id.shopping_Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        nameEditText = findViewById(R.id.newName);
        amountEditText = findViewById(R.id.newAmount);
        priceEditText = findViewById(R.id.newPrice);
        totalView = findViewById(R.id.total);
        context = getApplicationContext();

        itemViewModel = new ItemViewModel();
        listViewModel = new ListsViewModel();
        assoViewModel = new AssoViewModel();
        dateViewModel = new DateViewModel();



        cancelAlarm();
        itemViewModel.setShoppingDate();
        ArrayList<Item> items = itemViewModel.createShoppingItems();
        ArrayList<Association> displayAssos = assoViewModel.findAssociations(items);
        ArrayList<ItemList> lists =  listViewModel.findLists_forItem(displayAssos);

        ArrayList<Item> itemWithoutList = dateViewModel.createItems(items, displayAssos, lists);
        assoViewModel.createAssos(itemWithoutList);


        adapter = new ShoppingRecyclerAdapter(new ShoppingRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemBuy(ShoppingItem item) {
                if(totalView.getText().toString().equals("total")){
                    dateViewModel.setTotal(item.getPrice());
                }else{
                    dateViewModel.addToTotal(item.getPrice());

                }
            }
            @Override
            public void onCancel(ShoppingItem item) {
                if(totalView.getText().toString().equals("0.0")){
                    totalView.setText(R.string.total);
                }else{
                    dateViewModel.subtractTotal(item.getPrice());
                }
            }
        });
        recyclerView.setAdapter(adapter);
        observers();
    }

//all the observers
    public void observers(){
        dateViewModel.getLiveSpItems().observe(this, new Observer<ArrayList<ShoppingItem>>() {
            @Override
            public void onChanged(ArrayList<ShoppingItem> shoppingItems) {
                adapter.setItems(shoppingItems);
                adapter.notifyDataSetChanged();
            }
        });
        dateViewModel.getLiveTotal().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                totalView.setText(String.valueOf(aFloat));
            }
        });

    }
    public void addNew(View view) {
       String newName = nameEditText.getText().toString();
       String newAmount = amountEditText.getText().toString();
       String newPrice = priceEditText.getText().toString();
        if(ApproveNewData(newName,newAmount,newPrice)){
            if(newAmount.equals("")){
                newAmount="0";
            }
            if(newPrice.equals("")){
                newPrice = "0.f";
            }
            int item_Id = dateViewModel.addSPItem(newName, Integer.parseInt(newAmount), Float.parseFloat(newPrice));
            assoViewModel.addAsso(0,item_Id,Integer.parseInt(newAmount));
            itemViewModel.createItem(newName, "0",newPrice);
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        dateViewModel.createReport(Float.parseFloat(totalView.getText().toString()),dateViewModel.getShoppingItems());
        itemViewModel.syncAfterShopping(dateViewModel.getShoppingItems());
        itemViewModel.updateDbItems();
        assoViewModel.updateAssociations();
        //Item scheduledItem = itemViewModel.getScheduledItem();
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra("flag",1);
        startService(intent);

    }
    public void cancelAlarm(){
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra("name","");
        intent.putExtra("flag",-1);
        startService(intent);
    }
//check if data entered is not above limits
    private boolean ApproveNewData(String newName, String newAmount, String newPrice) {
        if(newName.equals("")){
            Toast.makeText(context, "Name field is empty", Toast.LENGTH_SHORT).show();
        }else if(newName.length() > MAX_CHARS) {
            Toast.makeText(context, "Name is above 20 Character's", Toast.LENGTH_SHORT).show();
        }else if(newAmount.length() > MAX_LENGTH){
            Toast.makeText(context, "Quantity cant be above 7 digits", Toast.LENGTH_SHORT).show();
        }else if(newPrice.length() > MAX_LENGTH){
            Toast.makeText(context, "Price cant be above 7 digits", Toast.LENGTH_SHORT).show();
        }else{
            return true;
        }
        return false;
    }
}