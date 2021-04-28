package com.ignas.android.groceryshoppingapp.View.Layer.Lists;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.AssoResources;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.util.ArrayList;

public class AssoViewModel extends ViewModel {
    private final AssoResources assoResources;


    private final MutableLiveData<ArrayList <Association>> currentLive = new MutableLiveData<>();

    public AssoViewModel(){
        assoResources = new AssoResources();
    }

    public LiveData<ArrayList<Association>> getCurrentLive() {
        return currentLive;
    }
    public ArrayList<Association> getCurrentAsso(){return currentLive.getValue();}


    public ArrayList<Association> setAsso(int list_Id){
        currentLive.setValue(assoResources.getAsso(list_Id));
        return currentLive.getValue();
    }
    public void addAsso(int list_Id,int item_Id,int quantity){
        currentLive.setValue(assoResources.addAsso(list_Id,item_Id,quantity));
    }
    public void deleteAsso(int item_Id){
        currentLive.setValue(assoResources.deleteAsso(item_Id,currentLive.getValue()));
    }

    public void updateAssociations() {
        assoResources.saveAssociations();
        assoResources.deleteAssociations();
    }

    public void removeListAssos(ItemList list) {
        ArrayList<Association> deleted = assoResources.getAsso(list.getList_Id());
        assoResources.severList(deleted);
    }

    public ArrayList<Association> apartOfList(Item item) {
        return assoResources.findItemAssos(item.getItem_id());

    }
}
