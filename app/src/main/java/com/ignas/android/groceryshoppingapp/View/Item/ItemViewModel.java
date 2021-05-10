package com.ignas.android.groceryshoppingapp.View.Item;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.ItemResources;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ShoppingItem;

import java.util.ArrayList;

public class ItemViewModel extends ViewModel {
    private final ItemResources itemResources;
    private final MutableLiveData<ArrayList<Item>> mLiveItems = new MutableLiveData<>();
    private final MutableLiveData<Item> mLiveSDate = new MutableLiveData<>();
    final private String TAG="log";


    public ItemViewModel() {
        itemResources = new ItemResources();
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


//create list of items that are been notified.(Shopping Day Items)
    public ArrayList<Item> createShoppingItems(){
        return itemResources.createShoppingItems(mLiveItems.getValue());
    }
    public void syncAfterShopping(ArrayList<ShoppingItem> spItems){
        itemResources.syncAfterShopping(spItems);
    }


//live methods
    public LiveData<ArrayList<Item>> getLiveItems() {
        return mLiveItems;
    }
    public LiveData<Item> getLiveShoppingDate() { return mLiveSDate;}
    }
