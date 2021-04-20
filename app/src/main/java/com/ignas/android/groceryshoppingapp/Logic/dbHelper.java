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

public class dbHelper extends BroadcastReceiver {
    private static final String TAG ="log" ;//extends ViewModel {
    private static dbHelper dbHelper =null ;
    Context mContext = null;

    private ArrayList<Item> app_items;
    private ArrayList<ItemList> app_lists;
    RealmDb db;

    public dbHelper() {
        db = new RealmDb();
        //db.removeAll();
        app_items=setItems();
        app_lists = setLists();
        addEmpty();
    }


    public static dbHelper getInstance(){
        if(dbHelper ==null){
           dbHelper = new dbHelper();
        }
        return dbHelper;
    }

    private ArrayList<ItemList> setLists() {
        return db.getLists();
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
    public Item update() {
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
            itemToBeScheduled =getNextItem_toSchedule();
        }else if (newItems.size()==1){
            db.addItem(newItems.get(0));
            itemToBeScheduled=getNextItem_toSchedule();
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
       app_items = db.getItems();
       if(app_items.size()>0){
           Item itemToBeScheduled = getNextItem_toSchedule();
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
    private Item getNextItem_toSchedule(){
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
    /*
   private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, input -> "Hello world from section: " + input);
    public void setIndex(int index) {mIndex.setValue(index);}
    public LiveData<String> getText() {return mText;}

 */

}