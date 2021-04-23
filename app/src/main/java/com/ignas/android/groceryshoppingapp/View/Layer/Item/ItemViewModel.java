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
    //unique item methods
    public void addItem(String newName, String newDays, String newPrice){
        ArrayList<Item> temp = items.getValue();
        Item item = new Item();
        item.setItemName(newName);
        if(newDays.equals("")){
            item.setLastingDays(0);
        }else{
            item.setLastingDays(Integer.parseInt(newDays));
        }
        if(newPrice.equals("")){
            item.setPrice(0.f);
        }else{
            item.setPrice(Float.parseFloat(newPrice));
        }
        temp.add(item);
        items.setValue(temp);
    }
    public void removeItem(int position){
        ArrayList<Item> temp = items.getValue();
        temp.remove(position);
        items.setValue(temp);
    }
    public void changeItem(int position, String newName, String newDays, String newPrice) {
        ArrayList<Item> tempArray = items.getValue();
        Item oldItem = tempArray.get(position);

        oldItem.setItemName(newName);
        if(newDays.equals("")){
            oldItem.setLastingDays(0);
        }else{
            oldItem.setLastingDays(Integer.parseInt(newDays));
        }
        if(newPrice.equals("")){
            oldItem.setPrice(0.f);
        }else{
            oldItem.setPrice(Float.parseFloat(newPrice));
        }
        items.setValue(tempArray);
    }



    public Item refresh_Db_Items(){
        return itemResources.update(items.getValue());
    }

    public LiveData<ArrayList<Item>> getLiveItems() {
        return items;
    }




}
