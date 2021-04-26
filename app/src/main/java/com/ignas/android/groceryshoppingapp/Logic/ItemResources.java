package com.ignas.android.groceryshoppingapp.Logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Service.Alarm;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemResources extends BroadcastReceiver {
    private static final String TAG ="log";

    Context mContext = null;
    RealmDb db;

    public ItemResources(){};
    public ItemResources(Context context) {
        db = new RealmDb();
        mContext = context;
        //db.removeAll();
    }

    public void setContext(Context context){
        mContext = context;
    }

    public ArrayList<Item> getItems(){
        return db.getItems();
    }

    //add/delete(update) items
    public Item update(ArrayList<Item> app_items) {
        Item itemToBeScheduled= null;
        boolean runningRemoved = false;
        db = new RealmDb();
        ArrayList<Item>dbItems = db.getItemsOffline();
        ArrayList<Item>newItems = new ArrayList<>();
        if(dbItems.size()!=0) {
            for (int i = 0; i < app_items.size(); i++) {

                while (i < dbItems.size() && dbItems.get(i).getItem_id() !=  app_items.get(i).getItem_id()) {
                    if(dbItems.get(i).isRunning()){
                        runningRemoved = true;
                    }
                    db.removeItem(dbItems.get(i));
                    dbItems.remove(dbItems.get(i));
                }
                if(i >= dbItems.size() || !dbItems.get(i).equals(app_items.get(i))){
                    newItems.add(app_items.get(i));
                }
            }
            if(dbItems.size()>app_items.size()){
                for(int i = app_items.size(); i < dbItems.size(); i++){
                    if(dbItems.get(i).isRunning()){
                        runningRemoved = true;
                    }
                    db.removeItem(dbItems.get(i));
                }
            }
        }else{
            newItems.addAll(app_items);
        }
        if(newItems.size()>1) {
            db.addItems(newItems);
            itemToBeScheduled = getNextItem_toSchedule(app_items);
        }else if (newItems.size()==1){
            db.addItem(newItems.get(0));
            itemToBeScheduled=getNextItem_toSchedule(app_items);
        }else if(runningRemoved){
            re_scheduleAlarm();
        }

        return itemToBeScheduled;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("log", "onReceive: received restart");
        mContext = context;
        re_scheduleAlarm();
    }


    //re-schedule service
    public void re_scheduleAlarm(){
        db = new RealmDb();
        ArrayList<Item>app_items = db.getItemsOffline();
       if(app_items.size()>0){
           Item itemToBeScheduled = getNextItem_toSchedule(app_items);
           if(itemToBeScheduled != null){
               Intent intent = new Intent(mContext,Alarm.class);
               intent.putExtra("name",itemToBeScheduled.getItemName());
               intent.putExtra("time",itemToBeScheduled.getRunOutDate().getTime());
               mContext.startService(intent);
               Log.i("log", "re_scheduleAlarm: "+itemToBeScheduled.getItemName());
           }
       }
    }

    //finds current running item/update's it and return next item to be scheduled.
    private Item getNextItem_toSchedule(ArrayList<Item> app_items){
        Item lowestDateItem = new Item(1);// set to now
        Item runningItem = null;


        for(int i=0;i<app_items.size();i++){
            Item currentItem = app_items.get(i);
            if(currentItem.isRunning()){
                int value = currentItem.getLastingDays();
                if(value !=0){
                    currentItem.setRunOutDate(value);
                }
                currentItem.setRunning(false);
                runningItem = currentItem;
            }
            if(lowestDateItem.getRunOutDate().compareTo(currentItem.getRunOutDate()) > 0){
                currentItem.setRunOutDate(currentItem.getLastingDays());
                lowestDateItem = currentItem;
            }
        }
        lowestDateItem.setRunning(true);

        if(runningItem!=null){
            if(runningItem.getItem_id() != lowestDateItem.getItem_id()){
                ArrayList<Item> list = new ArrayList<>();
                list.add(runningItem);
                list.add(lowestDateItem);
                db.addItems(list);
            }
        }else{
            db.addItem(lowestDateItem);
        }
        return lowestDateItem;
    }

}