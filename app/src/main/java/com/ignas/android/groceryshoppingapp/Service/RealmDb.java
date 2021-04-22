package com.ignas.android.groceryshoppingapp.Service;


import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmDb{
    final String TAG = "log";

    private Realm realm;

    public RealmDb() { realm = Realm.getDefaultInstance(); }
    /*
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
                 else if(results.getRunOutDate().compareTo(item.getRunOutDate()) > 0){
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
        */
    //get smallest date TODO ------ can be deleted maybe
    /*
    public ArrayList<Item> getSmallestDate(){
       ArrayList<Item> itemCopy = new ArrayList<>();
        try {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm -> {

            //.minimumDate("runOutDate")

            //check if this item is running
            Item runningItem = realm.where(Item.class)
                    .equalTo("running", true)
                    .findFirst();

            Number value = realm.where(Item.class)
                    .min("lastingDays");//TODO ------------- testing

            if (runningItem != null && value != null) {
                if (runningItem.getLastingDays() > value.intValue()) {
                    runningItem.setRunning(false);

                    Date runningDate = runningItem.getRunOutDate();
                    Calendar today = Calendar.getInstance();
                    if ((today.getTime()).compareTo(runningDate) > 0) {
                        runningItem.setLastingDays(runningItem.getLastingDays());
                    }
                    Item results = realm.where(Item.class)
                            .equalTo("lastingDays", value.intValue())
                            .findFirst();

                    if (results != null) {
                        results.setRunning(true);
                        Item copy = realm.copyFromRealm(results);
                        itemCopy.add(copy);
                    }
                }
            } else if (value !=null) {
                Item results = realm.where(Item.class)
                        .equalTo("lastingDays", value.intValue())
                        .findFirst();

                if (results != null) {
                    results.setRunning(true);
                    Item copy = realm.copyFromRealm(results);
                    itemCopy.add(copy);
                }
            }

        });
        } catch (Exception e) {
            Log.d("log", "getNextAlarm: did not get alarm"+e.getMessage());
        }finally{
            realm.refresh();
            realm.close();
        }

        return itemCopy;
    }
    */
    //  Get all items
    public ArrayList<Item> getItems() {
        ArrayList<Item> list = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                RealmResults<Item> results = realm.where(Item.class).findAll();
                if (results.size() != 0) {
                    list.addAll(realm.copyFromRealm(results));
                }
            });
        }catch (Exception e){
            Log.i(TAG, "getItems: failed"+e.getMessage());
        }finally{
            realm.refresh();
            realm.close();
        }
        return list;
    }
    // add/copy list of items
    public void addItems(ArrayList<Item>items){
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {

                    realm.copyToRealmOrUpdate(items);
                    Log.d("log", "execute: added items...");
            });
        }catch (Exception e){
            Log.d("log", "addItems: did not save"+e.getLocalizedMessage());
        }finally{
            realm.refresh();
            realm.close();
        }
    }
    //remove single item
    public void removeItem(Item item) {
        try {
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm -> {

                realm.where(Item.class)
                        .equalTo("item_Id", item.getItem_id())
                        .findFirst()
                        .deleteFromRealm();
        });
        } catch (Exception e) {
            Log.d("log", "delete item. not found ");
        }finally{
            realm.refresh();
            realm.close();
        }
    }
    // add single item to database
    public void addItem(Item item){
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                realm.copyToRealmOrUpdate(item);
                Log.d(TAG, "execute: added item...");
            });
        }catch (Exception e){
            Log.d(TAG, "addItem: error adding items");
        }finally{
            realm.refresh();
            realm.close();
        }
    }
    //remove all data from db


//  Lists methods--------------------------------------
// get all Lists
    public ArrayList<ItemList> getLists() {
        ArrayList<ItemList> lists = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {
                RealmResults<ItemList> results = realm.where(ItemList.class).findAll();
                if (results.size() != 0) {
                    lists.addAll(realm.copyFromRealm(results));
                }
            });
        }catch (Exception e){
            Log.i(TAG, "getItems: failed"+e.getMessage());
        }finally{
            realm.refresh();
            realm.close();
        }
        return lists;
    }
// add/copy list of items
    public void addLists(ArrayList<ItemList>lists){
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {

                realm.copyToRealmOrUpdate(lists);
                Log.i("log", "execute: added lists...");
            });
        }catch (Exception e){
            Log.i("log", "lists: did not save"+e.getLocalizedMessage());
        }finally{
            realm.refresh();
            realm.close();
        }
    }
//remove single list
    public void removeList(ItemList list) {
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {

                realm.where(ItemList.class)
                        .equalTo("list_Id", list.getList_Id())
                        .findFirst()
                        .deleteFromRealm();
                Log.i("log", "removeList:successful ");
            });
        } catch (Exception e) {
            Log.i("log", "delete list. not found ");
        }finally{
            realm.refresh();
            realm.close();
        }
    }
// add single list
    public void addList(ItemList list){
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm -> {

                realm.copyToRealmOrUpdate(list);
                Log.i(TAG, "execute: added list...");
            });
        }catch (Exception e){
            Log.i(TAG, "add list: error adding lists");
        }finally{
            realm.refresh();
            realm.close();
        }
    }



    //--- for reset----
    public void removeAll(){
        try{
            realm.beginTransaction();
            realm.deleteAll();
            realm.commitTransaction();
        }catch(Exception e){
            Log.d(TAG, "removeAll: error");
        }finally{
            realm.refresh();
            realm.close();
        }

    }

}
