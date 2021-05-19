package com.ignas.android.groceryshoppingapp.Logic;

import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ListResources {



    public ListResources() {}
    public ArrayList<ItemList> getLists() {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        ArrayList<ItemList> lists = new ArrayList<>();
        RealmResults<ItemList> results = realm.where(ItemList.class)
                .equalTo("deleteFlag",false)
                .findAll();
        if (results.size() != 0) {
            lists.addAll(realm.copyFromRealm(results));
        }
        realm.commitTransaction();
        realm.close();

        return lists;
    }

//lists methods
public ItemList createList(String listName,String shopName){
    ItemList newItemList = new ItemList(listName,shopName);
    addList(newItemList);
    return newItemList;
}

    public ItemList removeList(ItemList list) {
        list.setDeleteFlag(true);
        addList(list);
        return list;
    }

    public ItemList modifyList(String listName, String shopName, ItemList curList) {

        curList.setShopName(shopName);
        curList.setListName(listName);
        addList(curList);
        return curList;
    }
    public void addList(ItemList list){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> inRealm.copyToRealmOrUpdate(list));
        }catch (Exception e){
            Log.wtf("log", "add list: error adding lists "+e.getMessage());
        }
    }

    public List<ItemList> findLists(List<Association> assos) {
        List<ItemList> foundLists = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for(Association a : assos){
            ItemList result = realm.where(ItemList.class)
                    .equalTo("list_Id",a.getList_Id()).findFirst();

            if(result !=null){
                foundLists.add(realm.copyFromRealm(result));
            }

        }
        realm.commitTransaction();
        realm.close();

        return foundLists;
    }
}
