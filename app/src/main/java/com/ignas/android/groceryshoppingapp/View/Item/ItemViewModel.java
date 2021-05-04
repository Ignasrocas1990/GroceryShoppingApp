package com.ignas.android.groceryshoppingapp.View.Item;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ignas.android.groceryshoppingapp.Logic.ItemResources;
import com.ignas.android.groceryshoppingapp.Models.Item;

import java.util.ArrayList;
import java.util.Date;

public class ItemViewModel extends AndroidViewModel {
    private ItemResources itemResources;
    private MutableLiveData<ArrayList<Item>> items = new MutableLiveData<>();
    private MutableLiveData<Item> shoppingItemDate = new MutableLiveData<>();



    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemResources = new ItemResources(application);
        items.setValue(itemResources.getItems());
        shoppingItemDate.setValue(itemResources.getShoppingDateItem());
    }
    public void createItem(String newName, String newDays, String newPrice){
        items.setValue(itemResources.createItem( newName, newDays, newPrice,items.getValue()));
    }
    public Item findItem(int position){
        ArrayList<Item> temp = items.getValue();
        return temp.get(position);
    }

    public void removeItem(int position){
        ArrayList<Item> temp = items.getValue();
        itemResources.removeItem(temp.get(position));
        temp.remove(position);
        items.setValue(temp);
    }
    public void changeItem(int position, String newName, String newDays, String newPrice) {
        ArrayList<Item> tempArray = items.getValue();
       itemResources.modifyItem(tempArray.get(position),newName,newDays,newPrice);

        items.setValue(tempArray);
    }
    public void reSyncItems(){
        itemResources.reSyncItems(items.getValue());
    }


    public void updateDbItems(){
         itemResources.update(items.getValue());
    }

    public Item getScheduledItem() { return itemResources.getScheduledItem(items.getValue());}

//shopping date methods
    public void createShoppingDate(int item_Id, int lastingDays){
        shoppingItemDate.setValue(itemResources.createDateItem(item_Id,lastingDays));
    }
    public void delShoppingDate(){
        itemResources.deleteShoppingDate(shoppingItemDate.getValue());
    }


//live methods
    public LiveData<ArrayList<Item>> getLiveItems() {
        return items;
    }
    public LiveData<Item> getLiveShoppingDate() { return shoppingItemDate;}

    }
