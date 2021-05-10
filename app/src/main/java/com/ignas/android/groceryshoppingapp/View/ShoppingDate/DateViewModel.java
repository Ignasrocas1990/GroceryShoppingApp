package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.DateResources;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Models.ShoppingItem;

import java.util.ArrayList;
import java.util.Date;

public class DateViewModel extends ViewModel {

    private final MutableLiveData<Boolean> app_switch = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ShoppingItem>> liveSpItems = new MutableLiveData<>();
    private final MutableLiveData<Float> liveTotal = new MutableLiveData<>();
    private final DateResources dateResources;


    public DateViewModel(){
        dateResources = new DateResources();
        app_switch.setValue(dateResources.getDBSwitch());
    }

//notification switch methods
    public boolean getSwitch() {
        return app_switch.getValue();
    }
    public void setSwitch(boolean state) {
        app_switch.setValue(state);
    }
    public boolean updateSwitch(){
        return dateResources.updateSwitch(app_switch.getValue());
    }
    public ArrayList<ShoppingItem> getShoppingItems(){
        return liveSpItems.getValue();
    }

    public void createItems(ArrayList<Item> items, ArrayList<Association> displayAssos, ArrayList<ItemList> lists ){
        liveSpItems.setValue(dateResources.createItems(items,displayAssos,lists));
    }
    public void addSPItem(String name,int amount,float price){
        ShoppingItem addSPItem = dateResources.addSPItem(name, amount, price);
        ArrayList<ShoppingItem> liveSpItems = this.liveSpItems.getValue();
        if(liveSpItems==null){
            liveSpItems = new ArrayList<>();
        }
        liveSpItems.add(addSPItem);
        this.liveSpItems.setValue(liveSpItems);
    }
    //total methods (simple add/subtract/set)
    public void setTotal(float price) {
        liveTotal.setValue(price);
    }
    public void addToTotal(float price){
        try{
            liveTotal.setValue(liveTotal.getValue()+price);
        }catch(NullPointerException e){
            Log.i("log", "addToTotal: "+e.getMessage());
        }
    }
    public void subtractTotal(float price){
        try{
            liveTotal.setValue(liveTotal.getValue()-price);
        }catch(NullPointerException e){
            Log.i("log", "addToTotal: "+e.getMessage());
        }
    }
    public void createReport(float total,ArrayList<Item> items){
        dateResources.createReport(total,items);
    }

//live observational methods
    public LiveData<Boolean> getLiveSwitch(){return app_switch;}
    //live methods
    public LiveData<Float> getLiveTotal() {
        return liveTotal;
    }
    public LiveData<ArrayList<ShoppingItem>> getLiveSpItems() {
        return liveSpItems;
    }
}
