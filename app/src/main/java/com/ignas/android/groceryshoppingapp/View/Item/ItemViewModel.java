package com.ignas.android.groceryshoppingapp.View.Item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.ItemResources;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.util.ArrayList;
import java.util.List;

public class ItemViewModel extends ViewModel {
    private final ItemResources itemResources;
    private final MutableLiveData<ArrayList<Item>> mLiveItems = new MutableLiveData<>();
    private final MutableLiveData<Item> mLiveSDate = new MutableLiveData<>();
    private List<Association> boughtAssos = new ArrayList<>();
    private final MutableLiveData<List<ItemList>> boughtLists = new MutableLiveData<>();


    public ItemViewModel() {
        itemResources = new ItemResources();
        mLiveItems.setValue(itemResources.getItems());
        mLiveSDate.setValue(itemResources.getShoppingDateItem());
    }

    public ArrayList<Item> getBoughtItems(){
        return itemResources.findBoughtItems();

    }
    public Item createItem(String newName, String newDays, String newPrice){
         Item item = itemResources.createItem( newName, newDays, newPrice);
        ArrayList<Item > temp = mLiveItems.getValue();
        temp.add(item);
        mLiveItems.setValue(temp);
        return item;
    }

    public void changeItem(int position, String newName, String newDays, String newPrice) {
        ArrayList<Item> tempArray = mLiveItems.getValue();
        itemResources.modifyItem(tempArray.get(position),newName,newDays,newPrice);
        mLiveItems.setValue(tempArray);
    }
    public Item findItem(int position){
        ArrayList<Item> temp = mLiveItems.getValue();
        return temp.get(position);
    }

    public void removeItem(int position){
        ArrayList<Item> temp = mLiveItems.getValue();
        itemResources.removeItem(temp.get(position));
        temp.remove(position);
        mLiveItems.setValue(temp);
    }
    public void reSyncItems(){
        itemResources.reSyncItems(mLiveItems.getValue());
    }

    public void updateDbItems(){
         itemResources.update(mLiveItems.getValue(), mLiveSDate.getValue());
    }

//shopping date methods
    public void createShoppingDate(int lastingDays){
        mLiveSDate.setValue(itemResources.createDateItem(lastingDays, mLiveSDate.getValue()));
    }
    public void setShoppingDate(){
        mLiveSDate.setValue(null);
    }

//find item by its item_id
    public ArrayList<Item> findItemByIds(ArrayList<Association> item_Ids){
        return itemResources.findItemById(item_Ids);
    }

//finds instances of bought items that selected in the report drop down menu.
    public void itemQuery(int item_Id){
        if(item_Id!=-1){
            boughtAssos = itemResources.findBoughtInstances(item_Id);
            boughtLists.setValue(itemResources.findListsQuery(boughtAssos));
        }else{
            boughtAssos.clear();
        }

    }


    public void setBoughtAssos(){boughtAssos.clear();}
//live methods
    public LiveData<ArrayList<Item>> getLiveItems() {
        return mLiveItems;
    }
    public LiveData<Item> getLiveShoppingDate() { return mLiveSDate;}

    public List<Association> getBoughtAssos() {
        return boughtAssos;
    }

    public LiveData<List<ItemList>> getBoughtLists() {
        return boughtLists;
    }

    public Item reSyncCurrent(int position) {
        Item currentItem = findItem(position);
        itemResources.reSyncCurrent(currentItem);
        return currentItem;
    }
    public void syncBoughtItem(Item item){
        itemResources.reSyncCurrent(item);

    }
    public ArrayList<Item> getNotifiedItems() {
        return itemResources.getNotifiedItems(mLiveItems.getValue());
    }
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
