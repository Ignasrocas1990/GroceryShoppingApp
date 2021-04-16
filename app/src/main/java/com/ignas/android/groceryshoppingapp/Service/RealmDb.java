package com.ignas.android.groceryshoppingapp.Service;

import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Item;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmDb{
    final String TAG = "log";

    private Realm realm;

    public RealmDb() { realm = Realm.getDefaultInstance(); }

    public boolean needToReschedule(Item item){
        AtomicBoolean flag= new AtomicBoolean();
        flag.set(false);// Flag is for if sheduling needed

         realm = Realm.getDefaultInstance();
         realm.executeTransactionAsync(realm -> {
             try {
                 //check if this item is running
                 Item results = realm.where(Item.class)
                         .equalTo("running",true)
                         .findFirst();
                 if (results == null) {
                     flag.set(true);

                 } else if (results.getLastingDays() > item.getLastingDays()){//TODO-----------testing-------
                 /*else if(results.getRunOutDate().compareTo(item.getRunOutDate()) > 0){ */
                     results.setRunning(false);
                     flag.set(true);
                 }
                 item.setRunning(true);
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
         return flag.get();
    }
    //  Get all items
    public ArrayList<Item> getItems() {
        realm = Realm.getDefaultInstance();
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
            }, error -> Log.d(TAG, "onError: Item did not save"));
        realm.refresh();
    }
    //remove single item
    public void removeItem(Item item) {

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
        realm.refresh();
    }
    // add single item to database
    public void addItem(Item item){
        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction(){
            @Override
            public void execute(@NotNull Realm realm) {
                realm.copyToRealmOrUpdate(item);
                Log.d(TAG, "execute: added item...");
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: Item save");
            }
        }, error -> Log.d(TAG, "onError: Item did not save"));
        realm.refresh();
    }
    //remove all data from db
    public void removeAll(){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }
}
