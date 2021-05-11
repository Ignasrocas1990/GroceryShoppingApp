package com.ignas.android.groceryshoppingapp.Logic;

import android.content.Context;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;

public class ListResources {

    private ArrayList<ItemList> db_lists;
    private ArrayList<ItemList> toSave = new ArrayList<>();// merge
    private ArrayList<ItemList> toDelete = new ArrayList<>();
    private ItemList list_to_del;
    RealmDb db;

    public ListResources() {
        db = new RealmDb();
        db_lists =  db.getLists();

    }
    public ArrayList<ItemList> getLists() {
        ArrayList<ItemList> app_lists = new ArrayList<>();
        if(db_lists.size() != 0){
            app_lists.addAll(db_lists);
        }
        return app_lists;
    }

//lists methods
public ItemList createList(String listName,String shopName){
    ItemList newItemList = new ItemList(listName,shopName);
    toSave.add(newItemList);
    return newItemList;
}

    public ItemList removeList(ItemList list) {

        if(Check.listEquals(db_lists,list) && Check.listEquals(toSave,list)){    //check if old item(modified) removing
            toSave.remove(list);
            toDelete.add(list);
        }else if (Check.listEquals(db_lists,list)){                      //item is old not modified
            toDelete.add(list);
        }else{
            toSave.remove(list);                                //item is just created
        }
        return list;
    }
    //current list methods
    public ItemList getDeleteList(){
        return list_to_del;
    }

    public void setItemtoDel(ItemList item){
        list_to_del = item;
    }
    public void deletedCurrent() {
        list_to_del = null;
    }
    public ItemList modifyList(String listName, String shopName, ItemList curList) {
        if(!Check.listEquals(toSave,curList)){

            toSave.add(curList);
        }
        curList.setShopName(shopName);
        curList.setListName(listName);
        return curList;
    }
    public void updateLists(){

        if(toSave.size()>1){
            db.addLists(toSave);
        }else if(toSave.size()==1){
            db.addList(toSave.get(0));
        }
        if(toDelete.size()>1){
            db.removeLists(toDelete);
        }else if(toDelete.size()==1){
            db.removeList(toDelete.get(0));
        }
    }
}
