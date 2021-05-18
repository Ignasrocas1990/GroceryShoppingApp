package com.ignas.android.groceryshoppingapp.View.Lists;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.AssoResources;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.RealmResults;

public class AssoViewModel extends ViewModel {
    private final int NONE = 0;
    private final AssoResources assoResources;
    private final MutableLiveData<List<Association>> currentLive = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> dateDisplay = new MutableLiveData<>();

    //constructor
    public AssoViewModel(){
        assoResources = new AssoResources();
    }

    public LiveData<List<Association>> getCurrentLive() {
        return currentLive;
    }
    public List<Association> getCurrentAsso(){return currentLive.getValue();}


//find association and set it to current
    public void setAsso(int list_Id){
        RealmResults<Association> associations = assoResources.getAsso(list_Id);
        currentLive.setValue(assoResources.getCopyAssociations(associations));

    }


//add association
    public void addAsso(int list_Id,int item_Id,int quantity){
        currentLive.setValue(assoResources.addAsso(list_Id,item_Id,quantity,currentLive.getValue()));
    }

//del association
    public void deleteAsso(int item_Id){
        currentLive.setValue(assoResources.deleteAsso(item_Id,currentLive.getValue()));
    }
    public void removeListAssos(ItemList list) {
        List<Association> deleted = assoResources.getAsso(list.getList_Id());
        assoResources.severList(deleted);
    }
//compile shopping associations
    public HashMap<Integer, ArrayList<Association>> findShoppingAssos(ArrayList<ItemList> lists, ArrayList<Item> notifiedItems) {
        return assoResources.findShoppingAssos(lists,notifiedItems);
    }

    public List<Association> apartOfList(Item item) {
        List<Association> assos = assoResources.findItemAssos(item.getItem_id());
        if(assos !=null){
            return assos;
        }
        return assos;

    }
    public void removeItemsAssos(List<ItemList> removed, Item deleteItem){
        assoResources.severItem(removed,deleteItem);
    }

    public ArrayList<Association> findAssociations(ArrayList<Item> items){
        return assoResources.findAssociations(items);
    }
    public void onBuyBought(Association currentAsso, HashMap<Integer, ArrayList<Association>> shoppingAssos) {
        if(currentAsso.getList_Id()==0){
            assoResources.removeWildAsso(currentAsso,shoppingAssos);
        }else{
            assoResources.removeBoughtAsso(currentAsso,shoppingAssos);
        }
    }
    public void insertOnFlyItemAsso(Association asso, HashMap<Integer, ArrayList<Association>> shoppingAssos){
        assoResources.insertOnFlyItemAsso(asso,shoppingAssos);
    }
    public void markAsBought(int item_id, String amountString){
        Association bought = createTempAsso(item_id,amountString);
        bought.setDeleteFlag(true);
        bought.setBought(true);

        assoResources.addToSave(bought);
    }

    public Association createTempAsso(int item_id, String amountString) {
        int amount=0;
        if(!amountString.equals("")){
            amount= Integer.parseInt(amountString);
        }
        return new Association(item_id,amount);


    }
    public HashMap<String, List<Association>> findAssosByDate() {
        ArrayList<String>foundDates = new ArrayList<>();
        HashMap<String, List<Association>> groups = assoResources.groupAssosByDate();
        if(groups.keySet().isEmpty()){
            foundDates.add(0,"no items bought");
            dateDisplay.setValue(foundDates);
        }else{
            foundDates = new ArrayList<>(groups.keySet());
            foundDates.add(0,"select date");
        }
        dateDisplay.setValue(foundDates);
        return groups;
    }

    public LiveData<ArrayList<String>> getDateDisplay() {
        return dateDisplay;
    }
}