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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class ItemResources extends BroadcastReceiver {
    private static final String TAG ="log";
    private ArrayList<Item> toUpdate = new ArrayList<>();// merge
    private ArrayList<Item> toCreate = new ArrayList<>();// merge
    private ArrayList<Item> toDelete = new ArrayList<>();
    private Item running=null;
    Context mContext = null;
    RealmDb db;

    public ItemResources(){};
    public ItemResources(Context context) {
        db = new RealmDb();
        mContext = context;
        getRunning(getItems());

        //db.removeAll();
    }

    public void setContext(Context context){
        mContext = context;
    }
    public void getRunning(ArrayList<Item> items){
        Item temp=null;
        if(running == null){
            temp = items.stream()
                    .filter(Item::isRunning)
                    .findFirst()
                    .orElse(null);
        }
        if(temp == null && items.size()>1){
            getSmallestDate(items);

        }else if (items.size()==1){
            running = items.get(0);
            running.setRunning(true);
            toUpdate.add(running);
        }else{
            running = outOfSync(temp);
        }
    }
    public ArrayList<Item> getItems(){
        return db.getItems();
    }

    public Item update(){
        toCreate.addAll(toUpdate);
        if(toCreate.size()>1){
            db.addItems(toCreate);
        }else if(toCreate.size()!=0){
            db.addItem(toCreate.get(0));
        }
        if(toDelete.size()>1){
            db.deleteItems(toDelete);
        }else if(toDelete.size()!=0){
            db.removeItem(toDelete.get(0));
        }
        return running;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("log", "Item Resources.java : received restart");
        mContext = context;
        re_scheduleAlarm();
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
        if(running==null){
            running = newItem;
            newItem.setRunning(true);

        }else if(newItem.getRunOutDate().compareTo(running.getRunOutDate()) < 0){
            running.setRunning(false);
            toUpdate.add(running);

            running = newItem;
            newItem.setRunning(true);
        }
        current.add(newItem);
        toCreate.add(newItem);
        return current;
    }
    public void removeItem(Item itemToRemove,ArrayList<Item>allItems){
        if(running==null){
            getRunning(allItems);
        }
        else if(itemToRemove.getItem_id() == running.getItem_id()){
            if(allItems.size() !=1 ){
                getRunning(allItems);
            }else{
                running = null;
            }
        }
        if(toCreate.contains(itemToRemove)) {
            toCreate.remove(itemToRemove);
        }else{
            toDelete.add(itemToRemove);
        }
    }
    public void modifyItem(Item oldItem,String newName, String newDays, String newPrice,ArrayList<Item> allItems){

        oldItem.setItemName(newName);
        if(newDays.equals("")) {
            oldItem.setLastingDays(0);
        }else if(oldItem.getLastingDays() != Integer.parseInt(newDays)){
            oldItem.setLastingDays(Integer.parseInt(newDays));
            if(oldItem.getRunOutDate().compareTo(running.getRunOutDate()) < 0){
                getRunning(allItems);
            }
        }if(newPrice.equals("")){
            oldItem.setPrice(0.f);
        }else{
            oldItem.setPrice(Float.parseFloat(newPrice));
        }

        Item result = toCreate.stream()
                .filter(i ->i.getItem_id()==oldItem.getItem_id())
                .findFirst().orElse(null);

        if(result!=null){
            result = oldItem;
        }else{
            result = toUpdate.stream()
                    .filter(i ->i.getItem_id()==oldItem.getItem_id())
                    .findFirst().orElse(null);
            if(result!=null){
                result = oldItem;
            }else{
                toUpdate.add(oldItem);
            }
        }
    }


    //re-schedule service
    public void re_scheduleAlarm(){
        db = new RealmDb();
        ArrayList<Item>app_items = db.getItems();
       if(app_items.size()>0){
           Item itemToBeScheduled = getNextItem_toSchedule(app_items);
           if(itemToBeScheduled != null){
               db.addItems(toUpdate);
               Intent intent = new Intent(mContext,Alarm.class);
               intent.putExtra("name",itemToBeScheduled.getItemName());
               intent.putExtra("time",itemToBeScheduled.getRunOutDate().getTime());
               mContext.startService(intent);
               Log.i("log", "re_scheduleAlarm: "+itemToBeScheduled.getItemName());
           }
       }
    }


    //finds current running item/update's it and return next item to be scheduled.
    private Item getNextItem_toSchedule(ArrayList<Item> allItems){
        final Date now = new Item(1).getRunOutDate();// set to now
        Item lowestDateItem = allItems.get(0);
        Item currentItem = null;
        boolean saveFlag=false;

        for(int i=0;i<allItems.size();i++){
            saveFlag = false;
            currentItem = allItems.get(i);

            if(currentItem.isRunning()){
                currentItem.setRunning(false);
                saveFlag = true;
            }
            if(now.compareTo(currentItem.getRunOutDate()) > 0){ // extend if in the past
                currentItem.setRunOutDate(currentItem.getLastingDays());
                saveFlag = true;
            }
            if(lowestDateItem.getRunOutDate().compareTo(currentItem.getRunOutDate()) >= 0 ){
                lowestDateItem = currentItem;
            }

            if(saveFlag){
                toUpdate.add(currentItem);
            }
        }
        lowestDateItem.setRunning(true);
        return lowestDateItem;
    }

    //find smallest date item
    private void getSmallestDate(ArrayList<Item> allItems){
        Item now = new Item(1);// set to now
        Item lowestDateItem = allItems.get(0);
        Date currentItemDate;

        for(int i=0;i<allItems.size();i++) {
            boolean saveFlag = false;
            Item currentItem = allItems.get(i);
            currentItemDate = currentItem.getRunOutDate();

            if(now.getRunOutDate().compareTo(currentItemDate) > 0 ){
                currentItem.setRunOutDate(currentItem.getLastingDays());
                saveFlag=true;
            }
            if(lowestDateItem.getRunOutDate().compareTo(currentItemDate) >= 0 ){
                lowestDateItem = currentItem;
            }
            if(saveFlag){
                toUpdate.add(currentItem);
            }
        }
        running = lowestDateItem;
        running.setRunning(true);
        toUpdate.add(running);

    }
    //check if running is inValid(in the past/ not up to date)
    public Item outOfSync(Item item){
        Item now = new Item(1);
        if(now.getRunOutDate().compareTo(item.getRunOutDate()) > 0){
            item.setRunOutDate(item.getLastingDays());
        }
        return item;
    }
}