package com.ignas.android.groceryshoppingapp.Logic;

import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Models.ShoppingItem;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ItemResources{
    private static final String TAG ="log";
    private final HashMap<Integer,Item> toSave = new HashMap<>();
    private final ArrayList<Item> db_items;
    private final Item db_SDate;

    //Context mContext = null;
    RealmDb db;
    public ItemResources() {
       // data = new RealmDb1();
        db = new RealmDb();
        db_items = db.getItems();
        Log.i(TAG, "ItemResources: "+db_items.size());
        db_SDate = db_items.stream()
                .filter(i-> i.getItem_id() == Integer.MAX_VALUE)
                .findFirst().orElse(null);
    }


//copy db items to app.
    public ArrayList<Item> getItems(){
        ArrayList<Item> app_items=new ArrayList<>();
        if(db_items.size() != 0){
             app_items = new ArrayList<>();
            app_items.addAll(db_items);
        }
        if(db_SDate !=null){
            app_items.remove(db_SDate);
        }
        return app_items;

    }
//access db to save/del data
    public void update(ArrayList<Item> app_items, Item app_SDate){

        //check and add date item to be deleted.
        if(app_SDate != null){
            if(!toSave.containsKey(app_SDate.getItem_id())) {
                toSave.put(app_SDate.getItem_id(), app_SDate);
            }
        }else if(db_SDate!=null){
            db_SDate.setDeleteFlag(true);
            toSave.put(db_SDate.getItem_id(),db_SDate);
        }

        //updates data
        if(toSave.size()!=0){
            ArrayList<Item> temp = new ArrayList<>(toSave.values());
            db.addItems(temp);
        }
    }

    //create item with new data and current list
    public Item createItem(String newName, String newDays, String newPrice){
        Item newItem = new Item();
        newItem.setItemName(newName);
        if(newDays.equals("")){
            newItem.setLastingDays(0);
        }else{
            newItem.setLastingDays(Integer.parseInt(newDays));
        }
        if(newPrice.equals("")){
            newItem.setPrice(0.f);
        }else{
            newItem.setPrice(Float.parseFloat(newPrice));
        }
        toSave.put(newItem.getItem_id(),newItem);
        return newItem;
    }
    public void removeItem(Item itemToRemove){
        if(Check.itemEquals(db_items,itemToRemove) && toSave.containsKey(itemToRemove.getItem_id())){ //check if old item(modified) removing
            toSave.get(itemToRemove.getItem_id()).setDeleteFlag(true);

        }else if (Check.itemEquals(db_items,itemToRemove)){ //item is old not modified

            itemToRemove.setDeleteFlag(true);
            toSave.put(itemToRemove.getItem_id(),itemToRemove);
        }else{
            toSave.remove(itemToRemove.getItem_id()); //item is just created
        }

    }
    public void modifyItem(Item oldItem,String newName, String newDays, String newPrice){

        if(!toSave.containsKey(oldItem.getItem_id())){
            toSave.put(oldItem.getItem_id(),oldItem);
        }
        oldItem.setItemName(newName);
        if(newDays.equals("")) {
            oldItem.setLastingDays(0);
        }else if(oldItem.getLastingDays() != Integer.parseInt(newDays)){

            oldItem.setLastingDays(Integer.parseInt(newDays));

        }if(newPrice.equals("")){
            oldItem.setPrice(0.f);
        }else{
            oldItem.setPrice(Float.parseFloat(newPrice));
        }
    }

// gets all current data and updates its Running out Date (after NTF switch is on )
    public void reSyncItems(ArrayList<Item> values) {
        Item toSaveCurr = null;
        Log.i(TAG, "items are reSynced");
        for(Item current : values){
            if(current.isNotified()){
                if(toSave.containsKey(current.getItem_id())){
                    toSaveCurr = toSave.get(current.getItem_id());
                    toSaveCurr.setRunOutDate(current.getLastingDays());
                    toSaveCurr.setNotified(false);
                }else{
                    current.setRunOutDate(current.getLastingDays());
                    current.setNotified(false);
                    toSave.put(current.getItem_id(),current);
                }

            }
        }
    }
//extend items after shopping day
    public void syncAfterShopping(ArrayList<ShoppingItem> spItems) {
        Item toSaveCurr;
        for(Item item : db_items){
            ShoppingItem itemFound = spItems.stream().filter(ShoppingItem::isSelected)
                    .filter(spItem->spItem.getItem_Id()==item.getItem_id()).findFirst().orElse(null);
            if(itemFound!=null){
                if(toSave.containsKey(item.getItem_id())){
                    toSaveCurr = toSave.get(item.getItem_id());
                    toSaveCurr.setRunOutDate(item.getLastingDays());
                    toSaveCurr.setNotified(false);
                }else{
                    item.setRunOutDate(item.getLastingDays());
                    item.setNotified(false);
                    toSave.put(item.getItem_id(),item);
                }
            }
        }
    }

    public Item getShoppingDateItem() { return db_SDate; }

//create/updates shopping date item.
    public Item createDateItem(int lastingDays,Item app_DateItem) {

        if(app_DateItem == null){//it does not exists
            app_DateItem = new Item("Shopping",Integer.MAX_VALUE,lastingDays);
        }else{
            app_DateItem.setLastingDays(lastingDays);
        }

       return app_DateItem;
    }

    public void deleteShoppingDate(Item shoppingDateItem) {
        shoppingDateItem.setDeleteFlag(true);
        toSave.put(shoppingDateItem.getItem_id(),shoppingDateItem);
    }

//just removes items that not been notified(any item that is going to running out)
    public ArrayList<Item> getNotifiedItems(ArrayList<Item> items){
        ArrayList<Item> copy = new ArrayList<Item>();
        for(int i=0;i<items.size();i++){

            Item current = items.get(i);

            if(current.isNotified()){
                copy.add(current);
            }
        }
        return copy;
    }


    public void reSyncCurrent(Item item) {
            if(toSave.containsKey(item.getItem_id())){
                item = toSave.get(item.getItem_id());
                item.setRunOutDate(item.getLastingDays());
                item.setNotified(false);
            }else{
                item.setRunOutDate(item.getLastingDays());
                item.setNotified(false);
                toSave.put(item.getItem_id(),item);
            }

    }

    public ArrayList<Item> findItemById(ArrayList<Association> item_ids) {
        if(db_items.size() > 0){
            ArrayList<Item> results = new ArrayList<>();
            for(Association asso:item_ids) {
                Item current = db_items.stream().filter(i -> i.getItem_id() == asso.getItem_Id())
                        .findFirst().orElse(null);
                if(current != null){
                    results.add(current);
                }
            }
            return results;
        }
        return null;
    }

    public ArrayList<Item> findBoughtItems() {
        ArrayList<Item> copy = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Item> items  = realm.where(Item.class).findAll();
        for(int i=0;i< items.size();i++){
            Item curr = items.get(i);
            Association result = realm.where(Association.class)
                    .equalTo("item_Id", curr.getItem_id())
                    .equalTo("bought",true).findFirst();
            if(result!=null){
                copy.add(realm.copyFromRealm(curr));
            }

        }
        realm.close();
        return copy;
    }

    public RealmResults<Association> findBoughtInstances(int item_Id){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        RealmResults<Association> results =  realm.where(Association.class)
                .equalTo("bought",true)
                .equalTo("item_Id",item_Id).findAllAsync();

        realm.commitTransaction();
        realm.close();
        return results;
    }

    public List<Association> getCopiedAssos(RealmResults<Association> associations) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<Association> copy = realm.copyFromRealm(associations);
        realm.commitTransaction();
        realm.close();
        return copy;

    }
    public ArrayList<ItemList> findListsQuery(RealmResults<Association> associations) {
        ArrayList <ItemList> lists = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
       // RealmQuery query = realm.where(ItemList.class);
        for(Association curr: associations){
            ItemList itemList = realm.where(ItemList.class).equalTo("list_Id", curr.getList_Id()).findFirst();
            if(itemList != null){
                lists.add(realm.copyFromRealm(itemList));
            }
        }
        //itemList = query.findAll();
        realm.commitTransaction();
        realm.close();
        return lists;
    }

    public List<ItemList> copyLists(RealmResults<ItemList> itemLists) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<ItemList> copy = realm.copyFromRealm(itemLists);
        realm.commitTransaction();
        realm.close();
        return copy;
    }
}