package com.ignas.android.groceryshoppingapp.Logic;

import android.content.Context;
import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;
import java.util.Date;

public class ItemResources{
    private static final String TAG ="log";
    private final ArrayList<Item> toSave = new ArrayList<>();
    private final ArrayList<Item> toDelete = new ArrayList<>();
    private ArrayList<Item> db_items = new ArrayList<>();
    private Item db_SDate = null;

    Context mContext = null;
    RealmDb db;

    public ItemResources(){}

    public ItemResources(Context context) {
        db = new RealmDb();
        mContext = context;
        db_items = db.getItems();
        db_SDate = db_items.stream()
                .filter(i-> i.getItem_id() == Integer.MAX_VALUE)
                .findFirst().orElse(null);
    }

    public void setContext(Context context){
        mContext = context;
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
            toSave.add(app_SDate);
        } else if(db_SDate!=null){
            toDelete.add(db_SDate);
        }

        //updates data
        if(toSave.size()>1){
            db.addItems(toSave);
        }else if(toSave.size()!=0){
            db.addItem(toSave.get(0));
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
        toSave.add(newItem);
        return current;
    }
    public void removeItem(Item itemToRemove){
        if(Check.itemEquals(db_items,itemToRemove) && Check.itemEquals(toSave,itemToRemove)){ //check if old item(modified) removing
            toSave.remove(itemToRemove);
            toDelete.add(itemToRemove);
        }else if (Check.itemEquals(db_items,itemToRemove)){ //item is old not modified
            toDelete.add(itemToRemove);
        }else{
            toSave.remove(itemToRemove); //item is just created
        }

    }
    public void modifyItem(Item oldItem,String newName, String newDays, String newPrice){

        if(!Check.itemEquals(toSave,oldItem)){
            toSave.add(oldItem);
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
    //re-schedule service
    public Item re_scheduleAlarm(){
        db = new RealmDb();
        if(!db.getSwitch()){
            return null;
        }
        ArrayList<Item>app_items = db.getItems();
       if(app_items.size()>1){
           Item itemToBeScheduled = getScheduledItem(app_items);
           if(itemToBeScheduled != null){
               db.addItems(toSave);
               Log.i("log", "re_scheduleAlarm: "+itemToBeScheduled.getItemName()+
                       " lasting days of "+itemToBeScheduled.getLastingDays());

               return itemToBeScheduled;
           }
       }else if(app_items.size() == 1){
           Item itemToBeScheduled = app_items.get(0);
           itemToBeScheduled.setRunOutDate(itemToBeScheduled.getLastingDays());
           db.addItems(toSave);
           Log.i("log", "re_scheduleAlarm: "+itemToBeScheduled.getItemName()+
                   " lasting days of "+itemToBeScheduled.getLastingDays());

           return itemToBeScheduled;
       }
        return null;
    }
    //finds current running item/update's it and return next item to be scheduled.
    public Item getScheduledItem(ArrayList<Item> appItems){
        if(appItems.size()==0){
            return null;
        }else if(appItems.size()==1){
            return appItems.get(0);
        }
        final Date now = new Item(1).getRunOutDate();// set to now
        Item lowestDateItem = appItems.get(0);
        Item currentItem;

        for(int i=0;i<appItems.size();i++){
            currentItem = appItems.get(i);

            if(currentItem.isRunning()){
                currentItem=extendTime(currentItem); // extend items time
                currentItem.setRunning(false);
                toSave.add(currentItem);

            }
            if(lowestDateItem.getRunOutDate().compareTo(currentItem.getRunOutDate()) >= 0 ){
                lowestDateItem = currentItem;
            }
        }
        lowestDateItem.setRunning(true);

        if( !Check.itemEquals(toSave,lowestDateItem)){
            toSave.add(lowestDateItem);
        }
        return lowestDateItem;
    }

//check if running is inValid(in the past/ not up to date)
    public Item extendTime(Item item){
        Item now = new Item(1);
        if(now.getRunOutDate().compareTo(item.getRunOutDate()) > 0){
            item.setRunOutDate(item.getLastingDays());
        }
        return item;
    }
// gets all current data and updates its Running out Date (after NTF switch is on )
    public void reSyncItems(ArrayList<Item> value) {
        Log.i(TAG, "items are reSynced");
        for(Item current : value){
            current.setRunOutDate(current.getLastingDays());
        }
        if(value.size()>1){
            db.addItems(value);
        }else if(value.size()==1){
            db.addItem(value.get(0));
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
}