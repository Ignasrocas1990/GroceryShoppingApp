package com.ignas.android.groceryshoppingapp.Logic;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Alarm;
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
    public void onReceive(Context context, Intent intent) {

        ArrayList<Item> list = new RealmDb().getItems();
        //Item item = getSmallestDateItem(list);//
        Item item = test(list);//TODO-------------------------testing
        Alarm alert = new Alarm();
        alert.setMilliseconds(item.getLastingDays());
        alert.setAlarm(context, intent,item.getItemName());
    }
    //re-schedule service
    public void re_scheduleAlarm(){
       // ArrayList<Item> list = new RealmDb().getItems(); maybe not like that
        //Item item = getSmallestDateItem(list);//
        //Item item = test(list);
        ArrayList<Item> items = db.getSmallestDate();//TODO-------------------------testing
        if(items.size()>0){
            Item item = items.get(0);
            Alarm alert = new Alarm();
            alert.setMilliseconds(item.getLastingDays());
            alert.setAlarm(mContext, null,item.getItemName());
        }

    }


    //create new schedule service
    public void scheduleAlarm(){
        //ArrayList<Item> list = new RealmDb().getItems();
        //Item item = getSmallestDateItem(list);//
        //Item item = test(list);  NOTE: change it to getSmallestDate() instead (first check if it is not running)
        ArrayList<Item> items = db.getSmallestDate();//TODO-------------------------testing
        if(items.size()>0) {
            Item item = items.get(0);
            Alarm alert = new Alarm();
            alert.setMilliseconds(item.getLastingDays());
            alert.setAlarm(mContext, null, item.getItemName());
        }



    }

    //get Smallest number TODO ----for testing ------------------------
    private Item test(ArrayList<Item>newItems){

        Item lowestDateItem = newItems.get(0);
        for(int i=1;i<newItems.size();i++){
            Item currentItem = newItems.get(i);

            if(lowestDateItem.getLastingDays() > currentItem.getLastingDays()){
                lowestDateItem = currentItem;
            }
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