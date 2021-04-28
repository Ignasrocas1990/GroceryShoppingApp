package com.ignas.android.groceryshoppingapp.View.Layer.Lists;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ignas.android.groceryshoppingapp.Logic.ListResources;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.util.ArrayList;

public class ListsViewModel extends AndroidViewModel {

    private final MutableLiveData<ArrayList<ItemList>> lists = new MutableLiveData<>();
    private final ListResources listResources;

    private final MutableLiveData<ItemList> currentList = new MutableLiveData<>();
    private ItemList list_to_del;

    public ListsViewModel(@NonNull Application application) {
        super(application);
        listResources = new ListResources(application);
        lists.setValue(listResources.getLists());

    }
    //lists methods
    public void createList(String listName,String shopName){
        ArrayList<ItemList> oldList = lists.getValue();
        ItemList newItemList = new ItemList(listName,shopName);
        oldList.add(newItemList);
        lists.setValue(oldList);

        setCurrentList(newItemList);
    }
    public void removeList(ItemList list) {
        ArrayList<ItemList> allList = lists.getValue();
        allList.remove(list);

        lists.setValue(allList);
    }
//find list by list_Id
    public ItemList findList(int list_Id) {
        ArrayList<ItemList> allList = lists.getValue();
        ItemList curList = allList.stream()
                .filter(list -> list_Id == list.getList_Id())
                .findAny().orElse(null);

        return curList;
    }
//get lists that contain specific item
    public ArrayList<ItemList> findLists_forItem(ArrayList<Association> assos){
        ArrayList<ItemList> found = new ArrayList<>();
        for(Association asso : assos){

            ItemList curList = findList(asso.getList_Id());
            if(curList !=null){
                found.add(curList);
            }
        }
        return found;
    }


    public void refresh_Db_Lists(){
        listResources.updateLists(lists.getValue());
    }


    //current list methods
    public ItemList getDeleteList(){
        return list_to_del;
    }
    public void deleted(){ list_to_del=null;}

    public ItemList setItemtoDel(){
        list_to_del = currentList.getValue();
        return list_to_del;
    }
    public void setCurrentList(ItemList current){
        currentList.setValue(current);
    }
    public void modifyList(String listName, String shopName) {
        ItemList curList = getConvertedList();
        curList.setShopName(shopName);
        curList.setListName(listName);
        currentList.setValue(curList);
    }

    public ItemList getConvertedList(){
        return currentList.getValue();
    }

    //live data methods
    public LiveData<ItemList> getCurrLiveList() {
        return currentList;
    }
    public LiveData<ArrayList<ItemList>> getLiveLists() {
        return lists;
    }



}
