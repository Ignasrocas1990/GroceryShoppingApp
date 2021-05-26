package com.ignas.android.groceryshoppingapp.Logic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/***
 * Author:Ignas Rocas
 * Student Id: C00135830
 * Date: 28/05/2021
 * Purpose: Project, ViewModel responsible of lists
 */
public class ListsViewModel extends ViewModel {
//live storage
    private final Repository repository;
    private final MutableLiveData<ArrayList<ItemList>> mLiveLists = new MutableLiveData<>();
    private final MutableLiveData<ItemList> currentList = new MutableLiveData<>();
    private ItemList list_to_del;


    public ListsViewModel() {
        repository = Repository.getInstance();
        mLiveLists.setValue(repository.getLists());

    }
//CRUD Lists methods
    public void createList(String listName,String shopName){
        ArrayList<ItemList> oldList = mLiveLists.getValue();

        ItemList newItemList = new ItemList(listName,shopName);
        repository.saveList(newItemList);

        oldList.add(newItemList);
        mLiveLists.setValue(oldList);
        setCurrentList(newItemList);
    }

    public void removeList(ItemList list) {
        ArrayList<ItemList> allList = mLiveLists.getValue();
        list.setDeleteFlag(true);
        repository.saveList(list);
        allList.remove(list);
        mLiveLists.setValue(allList);
    }
//find list by list_Id
    public ItemList findList(int list_Id) {
        ArrayList<ItemList> allList = mLiveLists.getValue();
        ItemList curList = allList.stream()
                .filter(list -> list_Id == list.getList_Id())
                .findAny().orElse(null);
        return curList;
    }

//get lists that contain specific item
    public List<ItemList> findLists_forItem(List<Association> assos){
        ArrayList<ItemList> found = new ArrayList<>();
        for(Association asso : assos){
            if(asso.getList_Id() !=0){

                ItemList curList = findList(asso.getList_Id());
                if(curList != null){
                    found.add(curList);
                }
            }

        }
        return found;
    }
    //current list methods
    public ItemList getDeleteList(){
        return list_to_del;
    }
    public void deletedCurrent(){ list_to_del=null;}

    public ItemList setCurrentToDel(){
        list_to_del =  currentList.getValue();
        return currentList.getValue();
    }
    public void setCurrentList(ItemList current){
        currentList.setValue(current);
    }

    //check if list not in toUpdate Array and update it
    public void modifyList(String listName, String shopName) {
         ItemList curList =  currentList.getValue();
        curList.setShopName(shopName);
        curList.setListName(listName);
        repository.saveList(curList);

        currentList.setValue(curList);
    }

    public ItemList getConvertedList(){
        return currentList.getValue();
    }

//after each item bought checks other lists and removes any dubs
    public ArrayList<ItemList> removeEmptyLists(ArrayList<ItemList> lists, HashMap<Integer, ArrayList<Association>> shoppingAssos) {
        ItemList spinnerText;
        if(lists!=null) {
            int i=0;
            for (; i < lists.size(); i++) {
                ItemList currentList = lists.get(i);
                if (shoppingAssos.containsKey(currentList.getList_Id())) {
                    if (shoppingAssos.get(currentList.getList_Id()).size() == 0) {
                        lists.remove(i);
                        i--;
                    }
                } else {
                    lists.remove(i);
                    i--;
                }
            }
            if(lists.size()==0){
                 spinnerText = new ItemList();
                spinnerText.setToStringText("no items to buy");
                lists.add(0, spinnerText);
            } else if(!lists.get(0).getToStringText().equals("select a list")) {
                spinnerText = new ItemList();
                spinnerText.setToStringText("select a list");
                lists.add(0, spinnerText);
            }else{
                lists.get(0).setToStringText("select a list");
            }

        }
        return lists;
    }

    //live data methods
    public LiveData<ItemList> getCurrLiveList() {
        return currentList;
    }
    public LiveData<ArrayList<ItemList>> getLiveLists() {
        return mLiveLists;
    }

    public List<ItemList> findLists(List<Association> assos) {
        return repository.findLists(assos);
    }
}
