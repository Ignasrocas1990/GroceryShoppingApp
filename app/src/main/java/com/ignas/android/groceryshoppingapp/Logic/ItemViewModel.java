package com.ignas.android.groceryshoppingapp.Logic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Service.Repository;

import java.util.ArrayList;
/***
 * Author:Ignas Rocas
 * Student Id: C00135830
 * Date: 28/05/2021
 * Purpose: Project, ViewModel responsible of Items
 */
public class ItemViewModel extends ViewModel {
    private final Repository repository;
    private final MutableLiveData<ArrayList<Item>> mLiveItems = new MutableLiveData<>();
    private final MutableLiveData<Item> mLiveSDate = new MutableLiveData<>();

    public ItemViewModel() {
        repository = Repository.getInstance();

        mLiveItems.setValue(repository.getItems());
        mLiveSDate.setValue(repository.getShoppingDateItem());
    }

    public ArrayList<Item> getBoughtItems(){
        return repository.findBoughtItems();

    }
//creates item
    public Item createItem(String newName, String newDays, String newPrice){
        Item newItem = new Item();
        newItem.setItemName(newName);
        if(newDays.equals("")){
            newItem.setLastingDays(0);
        }else{
            newItem.setLastingDays(Integer.parseInt(newDays));
        }
        if(newPrice.equals("")){
            newItem.setPrice(0.f);
        }else{
            newItem.setPrice(Float.parseFloat(newPrice));
        }
        repository.saveItem(newItem);

        ArrayList<Item > temp = mLiveItems.getValue();
        temp.add(newItem);
        mLiveItems.setValue(temp);
        return newItem;
    }
//Change item
    public void changeItem(int position, String newName, String newDays, String newPrice) {
        ArrayList<Item> tempArray = mLiveItems.getValue();
        Item oldItem = tempArray.get(position);

        oldItem.setItemName(newName);
        if(oldItem.equals("")) {
            oldItem.setLastingDays(0);
        }else if(oldItem.getLastingDays() != Integer.parseInt(newDays)){

            oldItem.setLastingDays(Integer.parseInt(newDays));

        }if(newPrice.equals("")){
            oldItem.setPrice(0.f);
        }else{
            oldItem.setPrice(Float.parseFloat(newPrice));
        }
        repository.saveItem(oldItem);
        mLiveItems.setValue(tempArray);
    }
    public Item findItem(int position){
        ArrayList<Item> temp = mLiveItems.getValue();
        return temp.get(position);
    }

    public void removeItem(int position){
        ArrayList<Item> temp = mLiveItems.getValue();
        Item itemToRemove = temp.get(position);
        itemToRemove.setDeleteFlag(true);
        repository.saveItem(itemToRemove);
        temp.remove(position);
        mLiveItems.setValue(temp);
    }
//sets all items to start of their scheduling
    public void reSyncItems(){
        ArrayList<Item> items = mLiveItems.getValue();
        ArrayList<Item> toSave = new ArrayList<Item>();
        if(items != null){
            for(Item current : items){
                if(current.isNotified()){

                    current.setRunOutDate(current.getLastingDays());
                    current.setNotified(false);
                    toSave.add(current);
                }
            }
            repository.addItems(toSave);
        }

    }
    public void createShoppingDate(int lastingDays){
        Item app_DateItem = mLiveSDate.getValue();
        if(app_DateItem == null){//it does not exists
            app_DateItem = new Item("Shopping",Integer.MAX_VALUE,lastingDays);
            app_DateItem.setNotified(false);
        }else{
            app_DateItem.setLastingDays(lastingDays);
        }
        repository.saveItem(app_DateItem);
        mLiveSDate.setValue(app_DateItem);
    }

    public void removeShoppingDate(){

        repository.removeShoppingDate();
        mLiveSDate.setValue(null);
    }




//live methods
    public LiveData<ArrayList<Item>> getLiveItems() {
        return mLiveItems;
    }
    public LiveData<Item> getLiveShoppingDate() { return mLiveSDate;}

    public Item reSyncCurrent(int position) {

        Item item = findItem(position);
        item.setRunOutDate(item.getLastingDays());
        item.setNotified(false);
        repository.saveItem(item);
        return item;
    }
//re-sets single item of notification
    public void syncBoughtItem(Item item){
        item.setRunOutDate(item.getLastingDays());
        item.setNotified(false);
        repository.saveItem(item);

    }
//finds notified items
    public ArrayList<Item> getNotifiedItems() {

        ArrayList<Item> items = mLiveItems.getValue();
        ArrayList<Item> copy = new ArrayList<Item>();
        for(int i=0;i<items.size();i++){

            Item current = items.get(i);

            if(current.isNotified()){
                copy.add(current);
            }
        }
        return copy;
    }
//sets spinner starter text (for the look)
    public ArrayList<Item> setSpinnerText(ArrayList<Item> items){

        if(items.size() == 0){
            Item displayItem = new Item("no item bought",0);
            items.add(0,displayItem);
        }else{
            Item displayItem = new Item("select item",0);
            items.add(0,displayItem);
        }
        return items;
    }
}
