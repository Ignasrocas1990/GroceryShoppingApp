package com.ignas.android.groceryshoppingapp.View.Item;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ignas.android.groceryshoppingapp.Logic.ItemResources;
import com.ignas.android.groceryshoppingapp.Logic.ShoppingDay;
import com.ignas.android.groceryshoppingapp.Models.Item;

import java.util.ArrayList;

public class ItemViewModel extends AndroidViewModel {
    private final ItemResources itemResources;
    private final MutableLiveData<ArrayList<Item>> items = new MutableLiveData<>();
    private final MutableLiveData<Item> app_SDate = new MutableLiveData<>();
    ShoppingDay shoppingDay;




    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemResources = new ItemResources(application);
        items.setValue(itemResources.getItems());
        app_SDate.setValue(itemResources.getShoppingDateItem());
        shoppingDay = new ShoppingDay();
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
    public Item getScheduledItem() {
        Item shoppingItem = app_SDate.getValue();//get current shopping date item & adds to all items

        ArrayList<Item> appItems = items.getValue();
        if(shoppingItem != null){
            appItems.add(shoppingItem);
            shoppingItem = itemResources.getScheduledItem(appItems);//remove Shopping date item in case item is not closed after STOP.
            appItems.remove(shoppingItem);
            updateDbItems();
            return shoppingItem;
        }
        return itemResources.getScheduledItem(appItems);
    }

    public void reSyncItems(){
        itemResources.reSyncItems(items.getValue());
    }

    public void updateDbItems(){
         itemResources.update(items.getValue(), app_SDate.getValue());
    }



//shopping date methods
    public void createShoppingDate(int lastingDays){
        app_SDate.setValue(itemResources.createDateItem(lastingDays, app_SDate.getValue()));
    }
    public void delShoppingDate(){
        app_SDate.setValue(null);
    }

//create list of lowest items
    public void createShoppingItems(){
        items.setValue(shoppingDay.createShoppingItems(items.getValue()));
    }
    public int getLeftOver(Item item){
        return shoppingDay.getLeftOver(item);
    }



//live methods
    public LiveData<ArrayList<Item>> getLiveItems() {
        return items;
    }
    public LiveData<Item> getLiveShoppingDate() { return app_SDate;}
    }
