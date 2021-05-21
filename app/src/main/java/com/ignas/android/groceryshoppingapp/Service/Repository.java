package com.ignas.android.groceryshoppingapp.Service;

import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.AlarmSwitch;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Repository {
    static private final Repository repository = new Repository();
    private static final String TAG ="log";
    private Repository() {}

    public static Repository getInstance() {
        //-----------------------------------------clear();
        return repository;
    }
//--------------------- ITEM methods -------------

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
        realm.beginTransaction();
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
        realm.commitTransaction();
        realm.close();
        return lists;
    }
    // add single item to database
    public void saveItem(Item item){
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
//------------------LIST METHODS -------


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

    public void saveList(ItemList list){
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
//---------------ASSOCIATION METHODS-------

    //gets Associations if found  by list_id (filtering)
    public List<Association> getAsso(int list_Id){
        List<Association> copy = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Association> results = realm.where(Association.class)
                .equalTo("list_Id", list_Id)
                .equalTo("deleteFlag",false)
                .findAll();

        if(results.size()!=0){
            copy = realm.copyFromRealm(results);
        }

        realm.commitTransaction();
        realm.close();
        return copy;
    }

    public List<Association> findItemAssos(int item_Id){
        List<Association> assos = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Association> result = realm.where(Association.class)
                .equalTo("item_Id",item_Id)
                .equalTo("deleteFlag",false)
                .findAll();
        if(result.size()!=0){
            assos = realm.copyFromRealm(result);
        }
        return assos;
    }

    // remove item associations from list/s
    public void severItem(List<ItemList> removed, Item deleteItem){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int list_id;
        for(int i = 0;i<removed.size();i++){
            list_id = removed.get(i).getList_Id();
            Association result = realm.where(Association.class).equalTo("list_Id",list_id)
                    .equalTo("item_Id",deleteItem.getItem_id()).findFirst();
            if(result !=null) {
                result.setDeleteFlag(true);
            }
        }
        realm.commitTransaction();
        realm.close();


    }
    public List<Association> getBoughtAssos(){
        List<Association> copy = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        RealmResults<Association> results = realm.where(Association.class)
                .equalTo("bought", true).findAll();
        if(results.size()!=0){
            copy = realm.copyFromRealm(results);
        }

        realm.commitTransaction();
        realm.close();

        return copy;

    }

    public ArrayList<Association> getAllAssos(){

        ArrayList<Association> list = new ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()) {

            realm.executeTransaction(inRealm -> {

                RealmResults<Association> results = inRealm.where(Association.class)
                        .equalTo("deleteFlag",false)
                        .findAll();
                if (results.size() != 0) {
                    list.addAll(inRealm.copyFromRealm(results));
                }
            });
        }catch (Exception e){
            Log.d("log", "get Associations: failed");
        }
        return list;
    }


    public void saveAsso(Association asso){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> inRealm.insertOrUpdate(asso));
        }catch (Exception e){
            Log.i("log", "asso add single : not successful"+e.getMessage());
        }
    }
//---------------------DATE/ALARM methods---------

//get Alarm Switch
    public boolean getDBSwitch() {
        Boolean[] result = new Boolean[1];
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm->{

                AlarmSwitch tempSwitch = inRealm.where(AlarmSwitch.class)
                        .equalTo("deleteFlag",false)
                        .findFirst();
                if(tempSwitch!=null){
                    result[0] = tempSwitch.isSwitched();
                }else{
                    result[0] = false;
                }
            });
        }catch(Exception e){
            Log.i("log", "getSwitch: get, not successfully");
        }
        return result[0];
    }
    //modify alarm switch
    public void updateSwitch(Boolean state){
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm ->{
                AlarmSwitch tempSwitch = inRealm.where(AlarmSwitch.class)
                        .equalTo("deleteFlag",false)
                        .findFirst();
                if(tempSwitch == null){
                    AlarmSwitch alarmSwitch = new AlarmSwitch();
                    alarmSwitch.setSwitch(state);
                    inRealm.insertOrUpdate(alarmSwitch);
                }else{
                    tempSwitch.setSwitch(state);
                }
            });

        }catch(Exception e){
            Log.i("log", "setSwitch: did not set switch"+e.getMessage());
        }
    }

    //get smallest date to schedule (is the switch is on)
    public Item getSmallestDateItem(int item_Id){
        Item[] result = {null};
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm->{

                AlarmSwitch tempSwitch = inRealm.where(AlarmSwitch.class).findFirst();
                if(tempSwitch!=null) {
                    if (tempSwitch.isSwitched()) {
                        if(item_Id != 0) {
                            Item running = inRealm.where(Item.class)
                                    .equalTo("item_Id", item_Id).findFirst();

                            if(running != null){ running.setNotified(true);}

                        }
                        Date dateResults = inRealm.where(Item.class)
                                .equalTo("notified", false)
                                .equalTo("deleteFlag",false)
                                .greaterThan("lastingDays", 0)
                                .notEqualTo("item_Id", item_Id)
                                .minimumDate("runOutDate");

                        if(dateResults !=null){
                            Item itemResult = inRealm.where(Item.class)
                                    .equalTo("runOutDate",dateResults).findFirst();

                            if(itemResult!=null)
                                Log.wtf("log", "db,item: "+item_Id+" "+itemResult.getItem_id());

                            result[0] = (inRealm.copyFromRealm(itemResult));
                        }
                    }
                }
            });

        }catch(Exception e){
            Log.i("log", "getSmallestDateItem: "+e);
        }
        if(result[0] != null) {
            return result[0];
        }
        return null;
    }
//in-case want to remove all data
    private static void clear() {
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm->{
                inRealm.deleteAll();
            });

        }catch(Exception e){

        }
    }




}
