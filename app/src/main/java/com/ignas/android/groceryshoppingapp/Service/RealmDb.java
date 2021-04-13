package com.ignas.android.groceryshoppingapp.Service;

import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.annotations.PrimaryKey;


public class RealmDb {
    final String TAG = "log";

    private Realm realm;

    public RealmDb() {

        realm = Realm.getDefaultInstance();
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
    public void addItem(Item item){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.createObject(Item.class);
            }
        });
    }
    public void removeItems(ArrayList<Item>items) {
        realm.executeTransactionAsync(new Realm.Transaction(){
            @Override
            public void execute(@NotNull Realm realm) {
                for(Item item : items){
                    try {
                        Item removeItem = realm.where(Item.class).equalTo("item_id", item.getItem_id()).findFirst();
                        removeItem.deleteFromRealm();
                    }catch (IllegalArgumentException e){
                        Log.d(TAG, "deleting items. not found ");
                    }
                }
            }
            }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: Item deleted");
            }
            }, new Realm.Transaction.OnError() {
            @Override
            public void onError(@NotNull Throwable error) {
                Log.d(TAG, "onError: Item did not delete");
            }
        }
        );
    }

    @Override
    protected void finalize() throws Throwable {
        realm.close();
        super.finalize();
    }
}
