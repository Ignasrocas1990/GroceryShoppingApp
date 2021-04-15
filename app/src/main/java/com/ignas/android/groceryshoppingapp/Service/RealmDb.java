package com.ignas.android.groceryshoppingapp.Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;


public class RealmDb{
    final String TAG = "log";

    private Realm realm;

    public RealmDb() {

        realm = Realm.getDefaultInstance();
    }
    public void update(ArrayList<Item> app_items) {
        ArrayList<Item>dbItems = getItems();
        ArrayList<Item>newItems = new ArrayList<>();
        if(dbItems.size()!=0) {
                for (int i = 0; i < app_items.size(); i++) {

                    while (i < dbItems.size() && !dbItems.get(i).equals(app_items.get(i))) {
                        Item dbA = dbItems.get(i);
                        Item appA = app_items.get(i);
                        Log.d(TAG, "old: " + String.valueOf(dbA.getItem_id()) + " new: " + String.valueOf(appA.getItem_id()));
                        boolean aaaa = dbA == appA;
                        aaaa = dbA.equals(appA);

                        removeItem(dbItems.get(i));
                        dbItems.remove(dbItems.get(i));
                    }
                    if(i >= dbItems.size()){
                        newItems.add(app_items.get(i));
                    }
                }
                if(dbItems.size()>app_items.size()){
                    for(int index = app_items.size(); index < dbItems.size(); index++){
                        removeItem(dbItems.get(index));
                    }
                }

                if(newItems.size()!=0) {addItems(newItems);}
            }else{
                addItems(app_items);
            }
    }

    //------------create a list
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
    public void removeAll(){
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
    }
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
    public void addItem(Item item){
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(item);
            }
        });
    }
    public void removeItem(Item item) {
        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction(){
            @Override
            public void execute(@NotNull Realm realm) {
                    try {
                        Log.d(TAG, "execute: "+String.valueOf(item.getItem_id()));
                        realm.where(Item.class)
                                .equalTo("item_iD", item.getItem_id())
                                .findFirst()
                                .deleteFromRealm();
                    }catch (Exception e){
                        Log.d(TAG, "delete item. not found ");
                    }
            }
            });
    }
    private void test(ArrayList<Item> app_items) {
        realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction(){
            @Override
            public void execute(@NotNull Realm realm) {
                realm.deleteAll();
                realm.copyToRealmOrUpdate(app_items);
                Log.d(TAG, "execute: added items...");
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: Item save");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(@NotNull Throwable error) {
                Log.d(TAG, "onError: Item did not save");
            }
        });
    }

    @Override
    protected void finalize() throws Throwable {
        realm.close();
        super.finalize();
    }
}
