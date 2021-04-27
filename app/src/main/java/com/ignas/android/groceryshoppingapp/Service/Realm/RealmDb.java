package com.ignas.android.groceryshoppingapp.Service.Realm;


import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmDb{
    final String TAG = "log";

    private Realm realm;

    public RealmDb() { realm = Realm.getDefaultInstance(); }

    //  Get all items
    public ArrayList<Item> getItems() {
        ArrayList<Item> list = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(realm -> {
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
    public ArrayList<Item> getItemsOffline() {
        ArrayList<Item> list = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
                RealmResults<Item> results = realm.where(Item.class).findAll();
                if (results.size() != 0) {
                    list.addAll(realm.copyFromRealm(results));
                }
            realm.commitTransaction();
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
        try (Realm realm = Realm.getDefaultInstance()){
            realm.executeTransactionAsync(inRealm -> {
                inRealm.copyToRealmOrUpdate(items);

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
        try (Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm -> inRealm.where(Item.class)
                    .equalTo("item_Id", item.getItem_id())
                    .findFirst()
                    .deleteFromRealm());

        } catch (Exception e) {
            Log.d("log", "delete item. not found ");
        }finally{
            realm.refresh();
            realm.close();
        }
    }
    public void deleteItems(ArrayList<Item> toDelete) {
        try (Realm realm = Realm.getDefaultInstance()){
            for(Item currItem : toDelete){
                realm.executeTransaction(inRealm -> inRealm.where(Item.class)
                        .equalTo("item_Id", currItem.getItem_id())
                        .findFirst()
                        .deleteFromRealm());
            }
        } catch (Exception e) {
            Log.d("log", "delete items. not found ");
        }finally{
            realm.refresh();
            realm.close();
        }
    }
    // add single item to database
    public void addItem(Item item){
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransactionAsync(realm -> {
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


//  Lists methods--------------------------------------
    public ArrayList<ItemList> getLists() {
        ArrayList<ItemList> lists = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();

                RealmResults<ItemList> results = realm.where(ItemList.class).findAll();
                if (results.size() != 0) {
                    lists.addAll(realm.copyFromRealm(results));
                }

            realm.commitTransaction();
        }catch (Exception e){
            Log.i("log", "getItems: failed"+e.getMessage());
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
            realm.beginTransaction();
                realm.copyToRealmOrUpdate(lists);
            realm.commitTransaction();
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
            realm.beginTransaction();
                realm.where(ItemList.class)
                        .equalTo("list_Id", list.getList_Id())
                        .findFirst()
                        .deleteFromRealm();
            realm.commitTransaction();
            Log.i("log", "removeList:successful ");
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
            realm.beginTransaction();

            realm.copyToRealmOrUpdate(list);
            realm.commitTransaction();
            Log.i("log", "execute: added list...");
        }catch (Exception e){
            Log.i("log", "add list: error adding lists");
        }finally{
            realm.refresh();
            realm.close();
        }
    }

// association methods-----------------------------------

    public ArrayList<Association> getAllAssos(){

        ArrayList<Association> list = new ArrayList<>();
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            RealmResults<Association> results = realm.where(Association.class).findAll();
            if (results.size() != 0) {
                list.addAll(realm.copyFromRealm(results));
            }
            realm.commitTransaction();
        }catch (Exception e){
            Log.d("log", "get Associations: failed"+e.getMessage());
        }finally{
            realm.refresh();
            realm.close();
        }
        return list;
    }
    public void addMultipleAsso(ArrayList<Association>asso){
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();

            realm.copyToRealmOrUpdate(asso);

            realm.commitTransaction();
        }catch (Exception e){
            Log.i("log", "associations: did not save"+e.getLocalizedMessage());
        }finally{
            realm.refresh();
            realm.close();
        }

    }
    public void removeAsso(Association asso) {
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.where(Association.class)
                    .equalTo("asso_Id", asso.getAsso_Id())
                    .findFirst()
                    .deleteFromRealm();
            realm.commitTransaction();
            Log.i("log", "remove Asso :successful ");
        } catch (Exception e) {
            Log.i("log", "delete Asso. not successful "+e.getMessage());
        }finally{
            realm.refresh();
            realm.close();
        }
    }
    public void addSingeAsso(Association asso){
        try {
            realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(asso);
            realm.commitTransaction();
            Log.i("log", "asso add single : added successfully...");
        }catch (Exception e){
            Log.i("log", "asso add single : not successful");
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
            Log.d("log", "removeAll: error");
        }finally{
            realm.refresh();
            realm.close();
        }

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        realm.close();
    }


}
