package com.ignas.android.groceryshoppingapp.Logic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.Alarm;
import com.ignas.android.groceryshoppingapp.Service.RealmDb;

import java.util.ArrayList;

public class Resources extends BroadcastReceiver {
    private static final String TAG ="log" ;
    Context mContext = null;
    RealmDb db;

    public Resources(Context context) {
        db = new RealmDb();
        mContext = context;
    }
    public Resources(){}

    public ArrayList<ItemList> getLists() {
        return db.getLists();
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
        app_items.remove(app_items.size()-1);
        db = new RealmDb();
        ArrayList<Item>dbItems = db.getItems();
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
        ArrayList<Item>app_items = db.getItems();
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
        Item lowestDateItem = app_items.get(0);
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
                lowestDateItem = currentItem;
            }
        }
        lowestDateItem.setRunning(true);

        if(runningItem!=null){
            if(runningItem.getItem_id() != lowestDateItem.getItem_id()){
                ArrayList<Item> list = new ArrayList<Item>();
                list.add(runningItem);
                list.add(lowestDateItem);
                db.addItems(list);
            }
        }else{
            db.addItem(lowestDateItem);
        }
        return lowestDateItem;
    }

    public void updateLists(ArrayList<ItemList> app_lists) {
        int i;
        ArrayList<ItemList>dbLists = db.getLists();
        ArrayList<ItemList>newLists = new ArrayList<>();
        if(dbLists.size()!=0 && app_lists.size()>1) {
            for (i = 0; i < app_lists.size(); i++) {

                while (i < dbLists.size() && dbLists.get(i).getList_Id() !=  app_lists.get(i).getList_Id()) {
                    db.removeList(dbLists.get(i));
                    dbLists.remove(dbLists.get(i));
                }
                if(i >= dbLists.size() || !dbLists.get(i).equals(app_lists.get(i))){
                    newLists.add(app_lists.get(i));
                }
            }
            if(dbLists.size()>app_lists.size()) {
                for (i = app_lists.size(); i < dbLists.size(); i++) {
                    db.removeList(dbLists.get(i));
                }
            }
        }else{
            newLists.addAll(app_lists);
        }
        if(newLists.size()>1){
            db.addLists(newLists);
        }else if(newLists.size()==1){
            db.addList(newLists.get(0));
        }
    }
}