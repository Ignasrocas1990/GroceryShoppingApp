package com.ignas.android.groceryshoppingapp.View.Layer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ignas.android.groceryshoppingapp.Logic.dbHelper;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.util.ArrayList;

public class ViewModel extends AndroidViewModel {
    private dbHelper dbData;
    private MutableLiveData<ArrayList<Item>> items = new MutableLiveData<>();
    private MutableLiveData<ArrayList<ItemList>> lists = new MutableLiveData<>();

    public ViewModel(@NonNull Application application) {
        super(application);
        dbData= dbHelper.getInstance(application);
        items.setValue(dbData.getItems());
        lists.setValue(dbData.getApp_lists());
    }
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
    public void changeItem(Item item){
        ArrayList<Item> temp = items.getValue();
        Item result = temp.stream()
                .filter(tempItem ->item.getItem_id() == tempItem.getItem_id())
                .findAny()
                .orElse(null);
        if (result!=null){
            temp.remove(result);
            temp.add(item);
        }
        items.setValue(temp);
    }
    public Item refreshDB(){

        return dbData.update(items.getValue());
    }

    public LiveData<ArrayList<Item>> getLiveItems() {
        return items;
    }

    public LiveData<ArrayList<ItemList>> getLiveLists() {
        return lists;
    }


}
