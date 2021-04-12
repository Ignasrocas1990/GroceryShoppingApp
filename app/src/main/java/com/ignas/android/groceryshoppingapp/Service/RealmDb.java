package com.ignas.android.groceryshoppingapp.Service;

import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmDb {
    final String TAG = "log";
    Realm realm;

    public RealmDb() {
        realm = Realm.getDefaultInstance();
    }
    //get all the lists
    /*
    public RealmResults<ItemList> getAllLists(){
        RealmResults<ItemList> lists = realm.where(ItemList.class).findAll();
        return null;
    }
    public int getA(){
        return 1;
    }
     */
    //------------create a list
    public ArrayList<Item> getItems() {
        ArrayList<Item> list = new ArrayList<>();
        try {
            RealmResults<Item> results = realm.where(Item.class).findAll();
            list.addAll(realm.copyFromRealm(results));
        }catch (Exception e){
            Log.d(TAG, "getItems: "+e.getLocalizedMessage());
        }
        return list;
    }
    public void addItems(ArrayList<Item>items){
        realm.executeTransactionAsync(new Realm.Transaction(){

            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(items);
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "onSuccess: all good");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.d(TAG, "onError: something when wrong");
            }
        }
        );
    }
}
