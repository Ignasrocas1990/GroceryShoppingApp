package com.ignas.android.groceryshoppingapp.Service.Realm;


import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.AlarmSwitch;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Models.Report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class RealmDb{
    final String TAG = "log";


    public RealmDb(){
        //clear();
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

//  Get all items
    public ArrayList<Item> getItems() {
        ArrayList<Item> list = new ArrayList<>();
        Log.i("log", "getItems: getting items");
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> {

                RealmResults<Item> results = inRealm.where(Item.class)
                        .equalTo("deleteFlag",false)
                        .findAll();
                if (results.size() != 0) {
                    List<Item> temp = inRealm.copyFromRealm(results);
                    list.addAll(temp);
                }
            });
        }catch (Exception e){
            Log.i("log", "getItems: failed");
        }
        return list;
    }

    // add/copy list of items
    public void addItems(ArrayList<Item>items){

        try (Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm -> {

                inRealm.copyToRealmOrUpdate(items);

                Log.i("log", "Realm AddItems : save items");
            });
        }catch (Exception e){
            Log.i("log", "addItems: did not save");
        }
    }

// add single item to database
    public void addItem(Item item){
        try (Realm realm = Realm.getDefaultInstance()){

            realm.executeTransaction(inRealm -> {
                inRealm.copyToRealmOrUpdate(item);
                Log.d("log", "execute: added item...");
            });
        }catch (Exception e){
            Log.d(TAG, "addItem: error adding items");
        }
    }


//  Lists methods--------------------------------------
    public ArrayList<ItemList> getLists() {
        ArrayList<ItemList> lists = new ArrayList<>();
        try (Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm -> {

            RealmResults<ItemList> results = realm.where(ItemList.class)
                    .equalTo("deleteFlag",false)
                    .findAll();
                 if (results.size() != 0) {
                 lists.addAll(realm.copyFromRealm(results));
            }
        });
        }catch (Exception e){
            Log.i("log", "getItems: failed");
        }
        return lists;
    }
// add/copy list of items
    public void addLists(ArrayList<ItemList>lists){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> inRealm.copyToRealmOrUpdate(lists));
        }catch (Exception e){
            Log.i("log", "lists: did not save"+e.getMessage());
        }
    }

// add single list
    public void addList(ItemList list){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> inRealm.copyToRealmOrUpdate(list));
        }catch (Exception e){
            Log.wtf("log", "add list: error adding lists "+e.getMessage());
        }
    }

// association methods---------------------------------------------------------

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
    public void addMultipleAsso(ArrayList<Association>asso){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> inRealm.copyToRealmOrUpdate(asso));
        }catch (Exception e){
            Log.i("log", "associations: did not save");
        }

    }

    public void addSingeAsso(Association asso){
        try (Realm realm = Realm.getDefaultInstance()) {
            realm.executeTransaction(inRealm -> inRealm.copyToRealmOrUpdate(asso));
        }catch (Exception e){
            Log.i("log", "asso add single : not successful");
        }
    }

//get Alarm Switch
    public boolean getSwitch() {
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
            Log.i(TAG, "getSwitch: get, not successfully");
        }
        return result[0];
    }

//modify alarm switch
    public void setSwitch(Boolean state){
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm ->{
                AlarmSwitch tempSwitch = inRealm.where(AlarmSwitch.class)
                        .equalTo("deleteFlag",false)
                        .findFirst();
                if(tempSwitch == null){
                    AlarmSwitch alarmSwitch = new AlarmSwitch();
                    alarmSwitch.setSwitch(state);
                    inRealm.copyToRealmOrUpdate(alarmSwitch);
                }else{
                    tempSwitch.setSwitch(state);
                }
            });

        }catch(Exception e){
            Log.i(TAG, "setSwitch: did not set switch");
        }
    }
//add report to Realm
    public void addReport(Report report) {
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm->{
                inRealm.insert(report);
            });

        }catch(Exception e){
            Log.i("log", "addReport:fail "+e.getMessage());
        }
    }
//get reports from Realm
    public ArrayList<Report> getReports(){
        ArrayList<Report> reports = new ArrayList<>();
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm ->{
                RealmResults<Report> results = inRealm.where(Report.class).findAll();
                if(results.size() !=0 ){
                    reports.addAll(inRealm.copyFromRealm(results));
                    Log.i(TAG, "getReports: inside the results");
                }
            });

        }catch(Exception e){
            Log.i("log", "getReports: failed "+e.getMessage());
        }
        return reports;
    }
    private void clear() {
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(inRealm->{
                inRealm.deleteAll();
            });

        }catch(Exception e){

        }
    }
}
