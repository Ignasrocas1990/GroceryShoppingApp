package com.ignas.android.groceryshoppingapp.Logic;

import android.content.Context;

import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;

public class ListResources {

    private ArrayList<ItemList> toUpdate = new ArrayList<>();// merge
    private ArrayList<ItemList> toCreate = new ArrayList<>();// merge
    private ArrayList<ItemList> toDelete = new ArrayList<>();
    private ItemList list_to_del;
    Context mContext = null;
    RealmDb db;

    public ListResources(Context context) {
        db = new RealmDb();
        mContext = context;
    }
    public ArrayList<ItemList> getLists() {
        return db.getLists();
    }

    public void setContext(Context context){
        mContext = context;
    }
/*
    public void updateLists(ArrayList<ItemList> app_lists) {
        int i;
        ArrayList<ItemList>dbLists = db.getLists();
        ArrayList<ItemList>newLists = new ArrayList<>();
        if(dbLists.size()!=0) {
            for (i = 0; i < app_lists.size(); i++) {

                while (i < dbLists.size() && dbLists.get(i).getList_Id() !=  app_lists.get(i).getList_Id()) {
                    db.removeList(dbLists.get(i));
                    dbLists.remove(dbLists.get(i));
                }
                if(i >= dbLists.size() || !dbLists.get(i).equals(app_lists.get(i))){
                    newLists.add(app_lists.get(i));
                }
            }
            if(dbLists.size()>app_lists.size()) {
                for (i = app_lists.size(); i < dbLists.size(); i++) {
                    db.removeList(dbLists.get(i));
                }
            }
        }else{
            newLists.addAll(app_lists);
        }
        if(newLists.size()>1){
            db.addLists(newLists);
        }else if(newLists.size()==1){
            db.addList(newLists.get(0));
        }
    }
    */
//lists methods
public ItemList createList(String listName,String shopName){
    ItemList newItemList = new ItemList(listName,shopName);
    toCreate.add(newItemList);
    return newItemList;
}
    public ItemList removeList(ItemList list) {
        if(toUpdate.contains(list)){
            toUpdate.remove(list);
        }else if(toCreate.contains(list)){
            toCreate.remove(list);
        }else{
            toDelete.add(list);
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
    list_to_del =  removeList(list_to_del);
    list_to_del = null;
    }
    public ItemList modifyList(String listName, String shopName, ItemList curList) {

        if(!toUpdate.contains(curList)){
            toUpdate.add(curList);
        }
        curList.setShopName(shopName);
        curList.setListName(listName);
        return curList;
    }
    public void updateLists(){
        if(toUpdate.size()!=0){
            toCreate.addAll(toUpdate);
        }
        if(toCreate.size()>1){
            db.addLists(toCreate);
        }else if(toCreate.size()==1){
            db.addList(toCreate.get(0));
        }
        if(toDelete.size()>1){
            db.removeLists(toDelete);
        }else if(toDelete.size()==1){
            db.removeList(toDelete.get(0));
        }
    }
}
