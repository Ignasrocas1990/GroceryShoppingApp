package com.ignas.android.groceryshoppingapp.Logic;

import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class ItemResources{
    private static final String TAG ="log";

    public ItemResources() {
    }


//retrieve items from database
    public ArrayList<Item> getItems(){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        ArrayList<Item> items = new ArrayList<>();

        RealmResults<Item> results = realm.where(Item.class)
                .equalTo("deleteFlag",false)
                .notEqualTo("item_Id", Integer.MAX_VALUE)
                .findAll();
        if (results.size() != 0) {
            List<Item> temp = realm.copyFromRealm(results);
            items.addAll(temp);
        }
        realm.commitTransaction();
        realm.close();
        return items;
    }
    public Item getShoppingDateItem() {

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Item shoppingDate = null;
        Item result = realm.where(Item.class)
                .equalTo("item_Id",Integer.MAX_VALUE)
                .equalTo("deleteFlag",false).findFirst();

        if(result!=null){
            shoppingDate= realm.copyFromRealm(result);
        }

        realm.commitTransaction();
        realm.close();
        return shoppingDate;
    }

//create/updates shopping date item.
    public Item createDateItem(int lastingDays,Item app_DateItem) {

        if(app_DateItem == null){//it does not exists
            app_DateItem = new Item("Shopping",Integer.MAX_VALUE,lastingDays);
        }else{
            app_DateItem.setLastingDays(lastingDays);
        }
        addItem(app_DateItem);

       return app_DateItem;
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

        item.setRunOutDate(item.getLastingDays());
        item.setNotified(false);
        addItem(item);
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

    public List<Association> findBoughtInstances(int item_Id){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<Association> assos = new ArrayList<>();
        RealmResults<Association> results =  realm.where(Association.class)
                .equalTo("bought",true)
                .equalTo("item_Id",item_Id).findAll();
        if(results.size() !=0){
            assos = realm.copyFromRealm(results);
        }

        realm.commitTransaction();
        realm.close();
        return assos;
    }

    public ArrayList<ItemList> findListsQuery(@NotNull List<Association> associations) {
        ArrayList <ItemList> lists = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        //realm.beginTransaction();
        for(Association curr: associations){
            ItemList itemList = realm.where(ItemList.class)
                    .equalTo("list_Id", curr.getList_Id()).findFirst();
            if(itemList != null){
                lists.add(realm.copyFromRealm(itemList));
            }else{
                ItemList temp = new ItemList("All Lists","All Shops");
                lists.add(temp);
            }
        }
        //realm.commitTransaction();
        realm.close();
        return lists;
    }
    // add single item to database
    public void addItem(Item item){
        try (Realm realm = Realm.getDefaultInstance()){

            realm.executeTransaction(inRealm -> {
                inRealm.insertOrUpdate(item);
            });
        }catch (Exception e){
            Log.d(TAG, "addItem: error adding items");
        }
    }
    // add/copy list of items
    public void addItems(ArrayList<Item>items){

        try (Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm -> {

                inRealm.insertOrUpdate(items);

                Log.i("log", "Realm AddItems : save items");
            });
        }catch (Exception e){
            Log.i("log", "addItems: did not save");
        }
    }

    public void removeShoppingDate() {
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm->{
                Item spDate = inRealm.where(Item.class)
                        .equalTo("item_Id", Integer.MAX_VALUE).findFirst();

                if(spDate != null){
                    spDate.deleteFromRealm();
                    Log.i(TAG, "removeShoppingDate: removed");
                }

            });
        }catch (Exception e){
            Log.i(TAG, "removeShoppingDate: "+e.getMessage());
        }
    }
}