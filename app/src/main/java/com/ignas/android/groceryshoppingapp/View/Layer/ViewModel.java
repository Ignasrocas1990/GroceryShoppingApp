package com.ignas.android.groceryshoppingapp.View.Layer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ignas.android.groceryshoppingapp.Logic.Resources;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.util.ArrayList;

public class ViewModel extends AndroidViewModel {
    private Resources dbData;
    private MutableLiveData<ArrayList<Item>> items = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ItemList>> lists = new MutableLiveData<>();

    public ViewModel(@NonNull Application application) {
        super(application);
        dbData= new Resources(application);
        items.setValue(dbData.getItems());
        lists.setValue(dbData.getLists());
        addItem(new Item());
    }
    //unique item methods
    public void addItem(Item item){
        ArrayList<Item> temp = items.getValue();
        temp.add(item);
        items.setValue(temp);
    }
    public void removeItem(Item item){
        ArrayList<Item> temp = items.getValue();
        temp.remove(item);
        items.setValue(temp);
    }

    public void changeItem(int position, String newName, String newDays, String newQuantity, String newPrice) {
        ArrayList<Item> tempArray = items.getValue();
        Item oldItem = tempArray.get(position);

        oldItem.setItemName(newName);
        if(newQuantity.equals("")){
            oldItem.setAmount(0);
        }else{
            oldItem.setAmount(Integer.parseInt(newQuantity));
        }
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

    //lists methods
    public void createList(String listName,String shopName){
        ItemList list = new ItemList(listName,shopName);
        ArrayList<ItemList> oldList = lists.getValue();
        oldList.add(list);
        lists.setValue(oldList);
    }
    public void refresh_Db_Lists(){
        dbData.updateLists(lists.getValue());
    }

    public Item refresh_Db_Items(){
        return dbData.update(items.getValue());
    }

    public LiveData<ArrayList<Item>> getLiveItems() {
        return items;
    }

    public LiveData<ArrayList<ItemList>> getLiveLists() {
        return lists;
    }

}
