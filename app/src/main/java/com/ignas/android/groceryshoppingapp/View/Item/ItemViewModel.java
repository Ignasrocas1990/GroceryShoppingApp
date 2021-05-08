package com.ignas.android.groceryshoppingapp.View.Item;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ignas.android.groceryshoppingapp.Logic.ItemResources;
import com.ignas.android.groceryshoppingapp.Models.Item;

import java.util.ArrayList;

public class ItemViewModel extends AndroidViewModel {
    private final ItemResources itemResources;
    private final MutableLiveData<ArrayList<Item>> mLiveItems = new MutableLiveData<>();
    private final MutableLiveData<Item> mLiveSDate = new MutableLiveData<>();
    final private String TAG="log";


    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemResources = new ItemResources(application);
        mLiveItems.setValue(itemResources.getItems());
        mLiveSDate.setValue(itemResources.getShoppingDateItem());
    }
    public void createItem(String newName, String newDays, String newPrice){
        mLiveItems.setValue(itemResources.createItem( newName, newDays, newPrice, mLiveItems.getValue()));
    }
    public Item findItem(int position){
        ArrayList<Item> temp = mLiveItems.getValue();
        return temp.get(position);
    }

    public void removeItem(int position){
        ArrayList<Item> temp = mLiveItems.getValue();
        itemResources.removeItem(temp.get(position));
        temp.remove(position);
        mLiveItems.setValue(temp);
    }
    public void changeItem(int position, String newName, String newDays, String newPrice) {
        ArrayList<Item> tempArray = mLiveItems.getValue();
       itemResources.modifyItem(tempArray.get(position),newName,newDays,newPrice);
        mLiveItems.setValue(tempArray);
    }
    /*TODO DELETE
    public Item getScheduledItem() {
        Item shoppingItem = mLiveSDate.getValue();//get current shopping date item & adds to all items

        ArrayList<Item> appItems = mLiveItems.getValue();
        if(shoppingItem != null){
            appItems.add(shoppingItem);
            //shoppingItem = itemResources.getScheduledItem(appItems);//remove Shopping date item in case item is not closed after STOP.
            appItems.remove(shoppingItem);
            updateDbItems();
            return shoppingItem;
        }
        return itemResources.getScheduledItem(appItems);
    }

     */

    public void reSyncItems(){
        itemResources.reSyncItems(mLiveItems.getValue());
    }

    public void updateDbItems(){
         itemResources.update(mLiveItems.getValue(), mLiveSDate.getValue());
    }

//shopping date methods
    public void createShoppingDate(int lastingDays){
        mLiveSDate.setValue(itemResources.createDateItem(lastingDays, mLiveSDate.getValue()));
    }
    public void setShoppingDate(){
        mLiveSDate.setValue(null);
    }


//create list of items that are been notified.
    public ArrayList<Item> createShoppingItems(){
        //mLiveItems.setValue(itemResources.createShoppingItems(mLiveItems.getValue()));
        return itemResources.createShoppingItems(mLiveItems.getValue());
    }




//live methods
    public LiveData<ArrayList<Item>> getLiveItems() {
        return mLiveItems;
    }
    public LiveData<Item> getLiveShoppingDate() { return mLiveSDate;}
    }
