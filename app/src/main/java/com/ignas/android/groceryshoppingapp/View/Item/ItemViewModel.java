package com.ignas.android.groceryshoppingapp.View.Item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.ItemResources;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ShoppingItem;

import java.util.ArrayList;

public class ItemViewModel extends ViewModel {
    private final ItemResources itemResources;
    private final MutableLiveData<ArrayList<Item>> mLiveItems = new MutableLiveData<>();
    private final MutableLiveData<Item> mLiveSDate = new MutableLiveData<>();


    public ItemViewModel() {
        itemResources = new ItemResources();
        mLiveItems.setValue(itemResources.getItems());
        mLiveSDate.setValue(itemResources.getShoppingDateItem());
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


//create list of items that are been notified.(Shopping Day Items)
    public ArrayList<Item> createShoppingItems(){
        return itemResources.getNotifiedItems(mLiveItems.getValue());
    }
    public void syncAfterShopping(ArrayList<ShoppingItem> spItems){
        itemResources.syncAfterShopping(spItems);
    }
//find item by its item_id
    public ArrayList<Item> findItemByIds(ArrayList<Association> item_Ids){
        return itemResources.findItemById(item_Ids);
    }

//live methods
    public LiveData<ArrayList<Item>> getLiveItems() {
        return mLiveItems;
    }
    public LiveData<Item> getLiveShoppingDate() { return mLiveSDate;}

    public void reSyncCurrent(int position) {
        Item currentItem = findItem(position);
        itemResources.reSyncCurrent(currentItem);
    }
    public void syncBoughtItem(Item item){
        itemResources.reSyncCurrent(item);

    }
    public ArrayList<Item> getNotifiedItems() {
        return itemResources.getNotifiedItems(mLiveItems.getValue());
    }


}
