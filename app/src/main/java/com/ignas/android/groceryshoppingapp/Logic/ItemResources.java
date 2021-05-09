package com.ignas.android.groceryshoppingapp.Logic;

import android.content.Context;
import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ItemResources{
    private static final String TAG ="log";
    private final HashMap<Integer,Item> toSave = new HashMap<>();

    private final ArrayList<Item> toDelete = new ArrayList<>();
    private ArrayList<Item> db_items = new ArrayList<>();
    private Item db_SDate = null;

    //Context mContext = null;
    RealmDb db;

    public ItemResources() {
        db = new RealmDb();
        db_items = db.getItems();
        db_SDate = db_items.stream()
                .filter(i-> i.getItem_id() == Integer.MAX_VALUE)
                .findFirst().orElse(null);
    }

//copy db items to app.
    public ArrayList<Item> getItems(){
        ArrayList<Item> app_items=new ArrayList<>();
        if(db_items.size() != 0){
             app_items = new ArrayList<>();
            app_items.addAll(db_items);
        }
        if(db_SDate !=null){
            app_items.remove(db_SDate);
        }
        return app_items;

    }

//access db to save/del data
    public void update(ArrayList<Item> app_items, Item app_SDate){

        //check and add date item to be deleted.
        if(app_SDate != null){
            if(!toSave.containsKey(app_SDate.getItem_id())) {
                toSave.put(app_SDate.getItem_id(), app_SDate);
            }
        }else if(db_SDate!=null){
            toDelete.add(db_SDate);
        }

        //updates data
        if(toSave.size()!=0){
            db.addItems(new ArrayList<>(toSave.values()));
        }
        if(toDelete.size()>1){
            db.deleteItems(toDelete);
        }else if(toDelete.size()!=0){
            db.removeItem(toDelete.get(0));
        }
    }

    //create item with new data and current list
    public ArrayList<Item> createItem(String newName, String newDays, String newPrice,ArrayList<Item>current){
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
        current.add(newItem);
        toSave.put(newItem.getItem_id(),newItem);
        return current;
    }
    public void removeItem(Item itemToRemove){
        if(Check.itemEquals(db_items,itemToRemove) && toSave.containsKey(itemToRemove.getItem_id())){ //check if old item(modified) removing
            toSave.remove(itemToRemove.getItem_id());
            toDelete.add(itemToRemove);
        }else if (Check.itemEquals(db_items,itemToRemove)){ //item is old not modified
            toDelete.add(itemToRemove);
        }else{
            toSave.remove(itemToRemove.getItem_id()); //item is just created
        }

    }
    public void modifyItem(Item oldItem,String newName, String newDays, String newPrice){

        if(!toSave.containsKey(oldItem.getItem_id())){
            toSave.put(oldItem.getItem_id(),oldItem);
        }
        oldItem.setItemName(newName);
        if(newDays.equals("")) {
            oldItem.setLastingDays(0);
        }else if(oldItem.getLastingDays() != Integer.parseInt(newDays)){

            oldItem.setLastingDays(Integer.parseInt(newDays));

        }if(newPrice.equals("")){
            oldItem.setPrice(0.f);
        }else{
            oldItem.setPrice(Float.parseFloat(newPrice));
        }
    }
    //re-schedule service TODO -----maybe delete
    /*
    public Item re_scheduleAlarm(){

        db = new RealmDb();
        if(!db.getSwitch()){
            return null;
        }else{
            return db.getSmallestDateItem();
        }
    }
*/
/*
    //finds current running item/update's it and return next item to be scheduled.
    public Item getScheduledItem(ArrayList<Item> appItems){
        if(appItems.size()==0){
            return null;
        }else if(appItems.size()==1){
            Item item = appItems.get(0);
            item.setNotified(true);
            db.addItem(item);
            return item;
        }
        //final Date now = Calendar.getInstance().getTime();// set to now
        Item lowestDateItem = null;
        Item currentItem;

        for(int i=0;i<appItems.size();i++){
            currentItem = appItems.get(i);
            if(lowestDateItem==null && !currentItem.isNotified()){

                lowestDateItem = currentItem;

            }else if (!currentItem.isNotified() &&
                    lowestDateItem.getRunOutDate().compareTo(currentItem.getRunOutDate()) >= 0) {
                    lowestDateItem = currentItem;
            }

        }
        if(lowestDateItem !=null){

            lowestDateItem.setNotified(true);

            if( !Check.itemEquals(toSave,lowestDateItem)){
                toSave.add(lowestDateItem);
            }
            return lowestDateItem;
        }
         return null;

    }

*/
/*
//check if running is inValid(in the past/ not up to date)
    public Item extendTime(Item item){
        Item now = new Item(1);
        if(now.getRunOutDate().compareTo(item.getRunOutDate()) > 0){
            item.setRunOutDate(item.getLastingDays());
        }
        return item;
    }
 */
// gets all current data and updates its Running out Date (after NTF switch is on )
    public void reSyncItems(ArrayList<Item> values) {
        Item toSaveCurr = null;
        Log.i(TAG, "items are reSynced");
        for(Item current : values){
            if(current.isNotified()){
                if(toSave.containsKey(current.getItem_id())){
                    toSaveCurr = toSave.get(current.getItem_id());
                    toSaveCurr.setRunOutDate(current.getLastingDays());
                    toSaveCurr.setNotified(false);
                }else{
                    current.setRunOutDate(current.getLastingDays());
                    current.setNotified(false);
                    toSave.put(current.getItem_id(),current);
                }

            }
        }
    }

    public Item getShoppingDateItem() { return db_SDate; }

//create/updates shopping date item.
    public Item createDateItem(int lastingDays,Item app_DateItem) {

        if(app_DateItem == null){//it does not exists
            app_DateItem = new Item("Shopping",Integer.MAX_VALUE,lastingDays);
        }else{
            app_DateItem.setLastingDays(lastingDays);
        }

       return app_DateItem;
    }

    public void deleteShoppingDate(Item shoppingDateItem) {
        db.removeItem(shoppingDateItem);
    }

    //just removes items that not been notified(any item that is going to running out)
    public ArrayList<Item> createShoppingItems(ArrayList<Item> items){
        ArrayList<Item> copy = new ArrayList<Item>();
        for(int i=0;i<items.size();i++){

            Item current = items.get(i);

            if(current.isNotified()){
                copy.add(current);
            }
        }
        return copy;
    }
}