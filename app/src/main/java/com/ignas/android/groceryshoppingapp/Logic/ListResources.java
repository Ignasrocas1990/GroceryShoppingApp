package com.ignas.android.groceryshoppingapp.Logic;

import android.content.Context;

import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;

public class ListResources {

    private ArrayList<ItemList> db_lists = new ArrayList<>();
    private ArrayList<ItemList> toSave = new ArrayList<>();// merge
    private ArrayList<ItemList> toDelete = new ArrayList<>();
    private ItemList list_to_del;
    Context mContext = null;
    RealmDb db;

    public ListResources(Context context) {
        db = new RealmDb();
        mContext = context;
        db_lists = getLists();

    }
    public ArrayList<ItemList> getLists() {
        return db.getLists();
    }

    public void setContext(Context context){
        mContext = context;
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

    public void setItemtoDel(ItemList toDel){
        list_to_del = toDel;
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
