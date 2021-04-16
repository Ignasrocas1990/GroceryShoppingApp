package com.ignas.android.groceryshoppingapp.Logic;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Service.RealmDb;

import java.util.ArrayList;

public class dbHelper extends BroadcastReceiver {
    private static final String TAG ="log" ;//extends ViewModel {
    private static dbHelper dbHelper =null ;

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
    @Override
    public void onReceive(Context context, Intent intent) {

    }


    /*
   private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, input -> "Hello world from section: " + input);
    public void setIndex(int index) {mIndex.setValue(index);}
    public LiveData<String> getText() {return mText;}

 */

}