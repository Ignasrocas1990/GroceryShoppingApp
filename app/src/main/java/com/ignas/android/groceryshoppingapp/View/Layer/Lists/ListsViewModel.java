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

    public ListsViewModel(@NonNull Application application) {
        super(application);
        listResources = new ListResources(application);
        lists.setValue(listResources.getLists());

    }
    //lists methods

    public void createList(String listName,String shopName){
        ArrayList<ItemList> oldList = lists.getValue();
        ItemList newItemList =  listResources.createList(listName,shopName);
        oldList.add(newItemList);
        lists.setValue(oldList);
        setCurrentList(newItemList);
    }


    public void removeList(ItemList list) {
        ArrayList<ItemList> allList = lists.getValue();
        allList.remove(listResources.removeList(list));
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




    //current list methods
    public ItemList getDeleteList(){
        return listResources.getDeleteList();
    }
    public void deletedCurrent(){ listResources.deletedCurrent();}

    public ItemList setCurrentToDel(){
        listResources.setItemtoDel(currentList.getValue());
        return currentList.getValue();
    }
    public void setCurrentList(ItemList current){
        currentList.setValue(current);
    }

    //check if list not in toUpdate Array and update it
    public void modifyList(String listName, String shopName) {
        currentList.setValue(listResources.modifyList(listName,shopName,getConvertedList()));
    }

    public ItemList getConvertedList(){
        return currentList.getValue();
    }

//update database
    public void refresh_Db_Lists(){
        listResources.updateLists();
    }

    //live data methods
    public LiveData<ItemList> getCurrLiveList() {
        return currentList;
    }
    public LiveData<ArrayList<ItemList>> getLiveLists() {
        return lists;
    }



}
