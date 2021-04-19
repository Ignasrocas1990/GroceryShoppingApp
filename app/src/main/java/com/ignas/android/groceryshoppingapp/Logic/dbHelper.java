package com.ignas.android.groceryshoppingapp.Logic;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Alarm;
import com.ignas.android.groceryshoppingapp.Notification;
import com.ignas.android.groceryshoppingapp.Service.RealmDb;

import java.util.ArrayList;

public class dbHelper extends BroadcastReceiver {
    private static final String TAG ="log" ;//extends ViewModel {
    private static dbHelper dbHelper =null ;
    Context mContext = null;

    private ArrayList<Item> app_items;

    RealmDb db;

    public dbHelper() {
        db = new RealmDb();
        //db.removeAll();
        app_items=setItems();
        addEmpty();
    }

    public static dbHelper getInstance(){
        if(dbHelper ==null){
           dbHelper = new dbHelper();
        }
        return dbHelper;
    }

    public void setContext(Context context){
        mContext = context;
    }
    private ArrayList<Item> setItems(){
       return db.getItems();
    }

    public ArrayList<Item> getItems() {
        return app_items;
    }
    public void addEmpty(){
        if(app_items.size()==0 || !app_items.get(app_items.size()-1).getItemName().equals("")) {
            app_items.add(new Item());
        }
    }

    //add/delete(update) items
    public void update(ArrayList<Item> app_items) {
        this.app_items = app_items;
        db = new RealmDb();
        ArrayList<Item>dbItems = db.getItems();
        ArrayList<Item>newItems = new ArrayList<>();
        if(dbItems.size()!=0) {
            for (int i = 0; i < app_items.size(); i++) {

                while (i < dbItems.size() && !dbItems.get(i).equals(app_items.get(i))) {
                    if(dbItems.get(i).isRunning()){

                        //schedule---TODO-------------(cancel all(find/update running),get next smallest)
                    }
                    db.removeItem(dbItems.get(i));
                    dbItems.remove(dbItems.get(i));
                }
                if(i >= dbItems.size()){
                    newItems.add(app_items.get(i));
                }
            }
            if(dbItems.size()>app_items.size()){
                for(int i = app_items.size(); i < dbItems.size(); i++){
                    if(dbItems.get(i).isRunning()){
                        //schedule---TODO-------------
                    }
                    db.removeItem(dbItems.get(i));
                }
            }

        }else{
            newItems.addAll(app_items);
        }
        if(newItems.size()>1) {
            db.addItems(newItems);
            scheduleAlarm();
        }else if (newItems.size()==1){
            db.addItem(newItems.get(0));
            scheduleAlarm();
        }
    }
    @Override
    public void onReceive(Context context, Intent intent) { // TODO --- can be deleted maybe
        Log.i("log", "onReceive: received");
        mContext = context;
        re_scheduleAlarm();

    }// can be deleted if working


    //re-schedule service
    public void re_scheduleAlarm(){
        db = new RealmDb();
       app_items = db.getItems();
       if(app_items.size()>0){
           Item itemToBeScheduled = test();
           if(itemToBeScheduled != null){
               new Alarm().setAlarm(mContext,itemToBeScheduled.getItemName(),itemToBeScheduled.getRunOutDate());
               Log.i("log", "re_scheduleAlarm: "+itemToBeScheduled.getItemName());

           }
       }
    }

    //create new schedule service
    public void scheduleAlarm(){
        //Item item = getSmallestDateItem(list);//
        Item item = test();// get smallest lasting days
        //ArrayList<Item> items = db.getSmallestDate();//TODO-------------------------testing

        if(item != null) {
            new Alarm().setAlarm(mContext,item.getItemName(),item.getRunOutDate());
        }
    }

    //get Smallest number to be scheduled and switch to that number TODO ----for testing(using lasting days) ------------------------
    private Item test(){
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

    //get Smallest Date
    private Item getSmallestDateItem(ArrayList<Item>newItems){
        Item lowestDateItem = newItems.get(0);
        for(int i=1;i<newItems.size();i++){
            Item currentItem = newItems.get(i);

            if(lowestDateItem.getRunOutDate().compareTo(currentItem.getRunOutDate()) > 0){
                lowestDateItem = currentItem;
            }
        }
        return lowestDateItem;
    }
    /*
   private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, input -> "Hello world from section: " + input);
    public void setIndex(int index) {mIndex.setValue(index);}
    public LiveData<String> getText() {return mText;}

 */

}