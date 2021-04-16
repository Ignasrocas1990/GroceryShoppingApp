package com.ignas.android.groceryshoppingapp.Service;

import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;


public class RealmDb{
    final String TAG = "log";

    private Realm realm;

    public RealmDb() { realm = Realm.getDefaultInstance(); }

    public void saveAdd(Item item){
        realm = Realm.getDefaultInstance();
         realm.executeTransactionAsync(realm -> {
             try {
                 //check if this item is running
                 Item results = realm.where(Item.class)
                         .equalTo("running",true)
                         .findFirst();
                 if (results == null) {
                     //schedule---TODO-------------
                 } else if(results.getRunOutDate().compareTo(item.getRunOutDate()) > 0) {
                     //schedule---TODO-------------
                 }else{

                 }
             } catch (Exception e) {
                 Log.d(TAG, "getNextAlarm: did not get alarm");
             }
         }, new Realm.Transaction.OnSuccess() {
             @Override
             public void onSuccess() {

             }
         }, new Realm.Transaction.OnError() {
             @Override
             public void onError(Throwable error) {

             }
         });
    }
    //remove single item
    private void removeItem(Item item) {

        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(realm -> {
            try {
                realm.where(Item.class)
                        .equalTo("item_iD", item.getItem_id())
                        .findFirst()
                        .deleteFromRealm();
                realm.refresh();
            } catch (Exception e) {
                Log.d(TAG, "delete item. not found ");
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: item deleted");
            }
        });
    }
    //add/delete(update) items
    public void update(ArrayList<Item> app_items) {
        ArrayList<Item>dbItems = getItems();
        ArrayList<Item>newItems = new ArrayList<>();
        if(dbItems.size()!=0) {
                for (int i = 0; i < app_items.size(); i++) {

                    while (i < dbItems.size() && !dbItems.get(i).equals(app_items.get(i))) {
                        if(dbItems.get(i).isRunning()){
                            //schedule---TODO-------------
                        }
                        removeItem(dbItems.get(i));
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
                        removeItem(dbItems.get(i));
                    }
                }

                if(newItems.size()!=0) {
                    addItems(newItems);
                }
            }else{
            addItems(app_items); }
    }
    //  Get all items
    public ArrayList<Item> getItems() {
        ArrayList<Item> list = new ArrayList<>();
        try {
            RealmResults<Item> results = realm.where(Item.class).findAll();
            if(results.size()!=0){
                list.addAll(realm.copyFromRealm(results));
            }
        }catch (Exception e){
            Log.d(TAG, "getItems: "+e.getLocalizedMessage());
        }
        return list;
    }
    // add/copy list of items
    public void addItems(ArrayList<Item>items){
        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction(){
            @Override
            public void execute(@NotNull Realm realm) {
                    realm.copyToRealmOrUpdate(items);
                Log.d(TAG, "execute: added items...");
                }
            }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: Item save");
                }
            }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "onError: Item did not save");
            }
        });
    }
    // add single item to database
    public void addItem(Item item){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(item);
            }
        });
    }
    //remove all data from db
    public void removeAll(){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }
    @Override
    protected void finalize() throws Throwable {
        realm.close();
        super.finalize();
    }
}
