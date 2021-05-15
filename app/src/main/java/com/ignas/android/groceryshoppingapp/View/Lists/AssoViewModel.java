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

public class AssoViewModel extends ViewModel {
    private final int NONE = 0;
    private final AssoResources assoResources;
    private final MutableLiveData<ArrayList <Association>> currentLive = new MutableLiveData<>();
    private final MutableLiveData<HashMap<Integer, ArrayList<Association>>> shoppingAssos = new MutableLiveData<>();

    //constructor
    public AssoViewModel(){
        assoResources = new AssoResources();
    }

    public LiveData<ArrayList<Association>> getCurrentLive() {
        return currentLive;
    }
    public ArrayList<Association> getCurrentAsso(){return currentLive.getValue();}


//find association and set it to current
    public ArrayList<Association> setAsso(int list_Id){
        currentLive.setValue(assoResources.getAsso(list_Id));
        return currentLive.getValue();
    }
//get list of associations for per list id
    public ArrayList<Association> getAsso(int list_Id){
        return assoResources.getAsso(list_Id);
    }
/*
//find multiple association for each list as map (for shopping day)
    public HashMap<Integer,ArrayList<Association>> findAssosForList(ArrayList<ItemList> lists){
        HashMap<Integer,ArrayList<Association>> listMap = new HashMap<>();
        ArrayList<Association> temp;
        for(int i = 0;i< lists.size();i++){
            ItemList customerList = lists.get(i);
            temp = getAsso(customerList.getList_Id());
            if(temp.size()==0){
                lists.remove(i);
                i--;
            }else{
                listMap.put(customerList.getList_Id(),temp);
            }
        }
        return listMap;
    }

 */

//add association
    public void addAsso(int list_Id,int item_Id,int quantity){
        currentLive.setValue(assoResources.addAsso(list_Id,item_Id,quantity,currentLive.getValue()));
    }
    public void createAssos(ArrayList<Item>items){//TODO Deletion
        for(Item i : items){
            addAsso(NONE,i.getItem_id(),NONE);
        }
    }
    public Association findAssociation(int asso_Id){
       return assoResources.findAssoById(asso_Id);
    }
//del association
    public void deleteAsso(int item_Id){
        currentLive.setValue(assoResources.deleteAsso(item_Id,currentLive.getValue()));
    }
//update db with app associations
    public void updateAssociations() {
        assoResources.updateDB_Associations();
    }

    public void removeListAssos(ItemList list) {
        ArrayList<Association> deleted = assoResources.getAsso(list.getList_Id());
        assoResources.severList(deleted);
    }
//compile shopping associations
    public HashMap<Integer, ArrayList<Association>> findShoppingAssos(ArrayList<ItemList> lists, ArrayList<Item> notifiedItems) {
        return assoResources.findShoppingAssos(lists,notifiedItems);
    }

    public ArrayList<Association> apartOfList(Item item) {
        return assoResources.findItemAssos(item.getItem_id());

    }
    public void removeItemsAssos(ArrayList<ItemList> removed, Item deleteItem){
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
}