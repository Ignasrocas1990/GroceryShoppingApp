package com.ignas.android.groceryshoppingapp.Logic;

import android.content.Context;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;
import java.util.HashMap;

public class ListResources {

    private final HashMap<Integer,ItemList> db_lists = new HashMap<>();
    private ArrayList<ItemList> toSave = new ArrayList<>();// merge

    private ItemList list_to_del;
    RealmDb db;

    public ListResources() {
        db = new RealmDb();
        list_to_map(db.getLists());
    }
//converts db lists to a map.
    public void list_to_map(ArrayList<ItemList> lists){
        for(ItemList list : lists){
            db_lists.put(list.getList_Id(),list);
        }
    }
    public ArrayList<ItemList> getLists() {
        ArrayList<ItemList> app_lists = new ArrayList<>();
        if(db_lists.size() != 0){
            app_lists.addAll(db_lists.values());
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

        if(db_lists.containsKey(list.getList_Id()) && Check.listEquals(toSave,list)){    //check if old item(modified) removing
            toSave.get(list.getList_Id()).setDeleteFlag(true);

        }else if (db_lists.containsKey(list.getList_Id())){                      //item is old not modified
            list.setDeleteFlag(true);
            toSave.add(list);
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
        }else
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
    }
}
