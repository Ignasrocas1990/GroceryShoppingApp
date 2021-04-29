package com.ignas.android.groceryshoppingapp.Logic;

import android.content.Context;
import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;
import java.util.Date;

public class ItemResources{
    private static final String TAG ="log";
    //private ArrayList<Item> toUpdate = new ArrayList<>();// merge
    private ArrayList<Item> toSave = new ArrayList<>();// merge
    private ArrayList<Item> toDelete = new ArrayList<>();
    Context mContext = null;
    RealmDb db;

    public ItemResources(){}


    public ItemResources(Context context) {
        db = new RealmDb();
        mContext = context;

    }

    public void setContext(Context context){
        mContext = context;
    }

    public ArrayList<Item> getItems(){
        return db.getItems();
    }

    public Item update(ArrayList<Item> app_items){

        Item itemToBeScheduled;
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
    //gets next item to be scheduled
        itemToBeScheduled =  getNextItem_toSchedule(app_items);
        if(itemToBeScheduled !=null){
            Log.i("log", "item scheduled : "+itemToBeScheduled.getItemName()+" lasting days of "+itemToBeScheduled.getLastingDays());
        }
        return itemToBeScheduled;
    }

    //unique item methods
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
        if( Check.itmesEqual(toSave,itemToRemove)) {
            toSave.remove(itemToRemove);
        }else{
            toDelete.add(itemToRemove);
        }
    }
    public void modifyItem(Item oldItem,String newName, String newDays, String newPrice){

        if(!Check.itmesEqual(toSave,oldItem)){
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
        ArrayList<Item>app_items = db.getItems();
       if(app_items.size()>1){
           Item itemToBeScheduled = getNextItem_toSchedule(app_items);
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
    private Item getNextItem_toSchedule(ArrayList<Item> allItems){
        if(allItems.size()==0){
            return null;
        }else if(allItems.size()==1){
            return allItems.get(0);
        }
        final Date now = new Item(1).getRunOutDate();// set to now
        Item lowestDateItem = allItems.get(0);
        Item currentItem;

        for(int i=0;i<allItems.size();i++){
            currentItem = allItems.get(i);

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

        if( !Check.itmesEqual(toSave,lowestDateItem)){
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
}