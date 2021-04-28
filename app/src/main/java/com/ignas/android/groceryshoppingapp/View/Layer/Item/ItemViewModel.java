package com.ignas.android.groceryshoppingapp.View.Layer.Item;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ignas.android.groceryshoppingapp.Logic.ItemResources;
import com.ignas.android.groceryshoppingapp.Logic.ListResources;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.util.ArrayList;

public class ItemViewModel extends AndroidViewModel {
    private ItemResources itemResources;
    private MutableLiveData<ArrayList<Item>> items = new MutableLiveData<>();



    public ItemViewModel(@NonNull Application application) {
        super(application);
        itemResources = new ItemResources(application);
        items.setValue(itemResources.getItems());
    }
    public void addItem(String newName, String newDays, String newPrice){
        items.setValue(itemResources.createItem( newName, newDays, newPrice,items.getValue()));
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



    public Item refresh_Db_Items(){
        return itemResources.update(items.getValue());
    }

    public LiveData<ArrayList<Item>> getLiveItems() {
        return items;
    }




}
