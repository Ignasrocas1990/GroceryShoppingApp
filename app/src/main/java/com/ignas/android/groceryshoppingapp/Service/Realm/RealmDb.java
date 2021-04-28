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


    public RealmDb() {}

    //  Get all items
    public ArrayList<Item> getItems() {
        ArrayList<Item> list = new ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> {
                RealmResults<Item> results = inRealm.where(Item.class).findAll();
                if (results.size() != 0) {
                    list.addAll(inRealm.copyFromRealm(results));
                }
            });
        }catch (Exception e){
            Log.i("log", "getItems: failed");
        }
        return list;
    }

    // add/copy list of items
    public void addItems(ArrayList<Item>items){
        try (Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm -> {
                inRealm.copyToRealmOrUpdate(items);

                Log.i("log", "Realm AddItems : save items");
            });
        }catch (Exception e){
            Log.i("log", "addItems: did not save"+e.getMessage());
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
        }
    }
    // add single item to database
    public void addItem(Item item){
        try (Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm -> {
                inRealm.copyToRealmOrUpdate(item);
                Log.d(TAG, "execute: added item...");
            });
        }catch (Exception e){
            Log.d(TAG, "addItem: error adding items");
        }
    }


//  Lists methods--------------------------------------
    public ArrayList<ItemList> getLists() {
        ArrayList<ItemList> lists = new ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm -> {
            RealmResults<ItemList> results = realm.where(ItemList.class).findAll();
                 if (results.size() != 0) {
                 lists.addAll(realm.copyFromRealm(results));
            }
        });
        }catch (Exception e){
            Log.i("log", "getItems: failed"+e.getMessage());
        }
        return lists;
    }
// add/copy list of items
    public void addLists(ArrayList<ItemList>lists){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> {
                inRealm.copyToRealmOrUpdate(lists);
            });
        }catch (Exception e){
            Log.i("log", "lists: did not save"+e.getLocalizedMessage());
        }
    }
//remove single list
    public void removeList(ItemList list) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> {
                inRealm.where(ItemList.class)
                        .equalTo("list_Id", list.getList_Id())
                        .findFirst()
                        .deleteFromRealm();
            });
        } catch (Exception e) {
            Log.i("log", "delete list. not found ");
        }
    }
// add single list
    public void addList(ItemList list){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> {

                inRealm.copyToRealmOrUpdate(list);
            });
        }catch (Exception e){
            Log.i("log", "add list: error adding lists");
        }
    }

// association methods-----------------------------------

    public ArrayList<Association> getAllAssos(){

        ArrayList<Association> list = new ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()) {

            realm.executeTransaction(inRealm -> {

                RealmResults<Association> results = inRealm.where(Association.class).findAll();
                if (results.size() != 0) {
                    list.addAll(inRealm.copyFromRealm(results));
                }
            });
        }catch (Exception e){
            Log.d("log", "get Associations: failed"+e.getMessage());
        }
        return list;
    }
    public void addMultipleAsso(ArrayList<Association>asso){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> {
                inRealm.copyToRealmOrUpdate(asso);
            });
        }catch (Exception e){
            Log.i("log", "associations: did not save"+e.getLocalizedMessage());
        }

    }
    public void removeAsso(Association asso) {
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> {
                inRealm.where(Association.class)
                        .equalTo("asso_Id", asso.getAsso_Id())
                        .findFirst()
                        .deleteFromRealm();
            });
        } catch (Exception e) {
            Log.i("log", "delete Asso. not successful "+e.getMessage());
        }
    }
    public void addSingeAsso(Association asso){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm ->{
                inRealm.copyToRealmOrUpdate(asso);
            });
            Log.i("log", "asso add single : added successfully...");
        }catch (Exception e){
            Log.i("log", "asso add single : not successful");
        }
    }



    //--- for reset----
    public void removeAll(){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm ->{
                inRealm.deleteAll();
            });

        }catch(Exception e){
            Log.d("log", "removeAll: error");
        }

    }
}
