package com.ignas.android.groceryshoppingapp.Logic;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.Realm;
import io.realm.RealmResults;

public class AssoResources {

    private final RealmDb db;

    public AssoResources() {
        db = new RealmDb();
    }

//copy associations from Realm
    public List<Association> getCopyAssociations(RealmResults<Association> assos){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        List<Association> result = realm.copyFromRealm(assos);

        realm.commitTransaction();
        realm.close();
        return result;
    }

//gets Associations if found  by list_id (filtering)
    public RealmResults<Association> getAsso(int list_Id){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        RealmResults<Association> results = realm.where(Association.class)
                .equalTo("list_Id", list_Id)
                .equalTo("deleteFlag",false)
                .findAll();

        realm.commitTransaction();
        realm.close();
        return results;
    }

//add new Association
    public List<Association> addAsso(int list_Id, int item_Id, int quantity, List<Association> current){
        Association newAsso = new Association(list_Id, item_Id, quantity);
        db.addSingeAsso(newAsso);

        current.add(newAsso);
        return current;
    }


//delete single association method from current live list of associations
    public List<Association> deleteAsso(int item_Id, List<Association> curr) {
//find selected association
        Association asso = curr.stream()
                .filter(a -> a.getItem_Id() == item_Id)
                .findFirst().orElse(null);

        if (asso != null) {
            asso.setDeleteFlag(true);
            db.addSingeAsso(asso);
            curr.remove(asso);
        }
        return curr;

    }
//set multiple items to be deleted from one list
    public void severList(List<Association> listToDelete){
        if(listToDelete.size() !=0){

                for(int i=0;i<listToDelete.size();i++){

                deleteAsso(listToDelete.get(i).getItem_Id(),listToDelete);
                i--;
            }
        }
    }

    public List<Association> findItemAssos(int item_Id){
        List<Association> assos;
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Association> result = realm.where(Association.class)
                .equalTo("item_Id",item_Id).findAll();
        if(result.size()!=0){
            assos = realm.copyFromRealm(result);
        }
        return result;

        /*
        ArrayList<Association> foundAssos = new ArrayList<>();
        for(ArrayList<Association> assoIndexArray : app_assos.values()){
            if(assoIndexArray.size() != 0){
                curAsso = assoIndexArray.stream()
                        .filter(asso->asso.getItem_Id() == item_Id)
                        .findFirst().orElse(null);

                if(curAsso!=null){
                    foundAssos.add(curAsso);
                }
            }
        }

         */
        //return foundAssos;
    }

// remove item associations from list/s
    public void severItem(List<ItemList> removed, Item deleteItem){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        int list_id;
        ArrayList<Association> currentAsso;
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
//find item associations to list for each item
    public ArrayList<Association> findAssociations(ArrayList<Item> items) {
        ArrayList<Association> associations = new ArrayList<>();
        for(Item item : items){
            associations.addAll(findItemAssos(item.getItem_id()));
        }
        return associations;
    }
//create temporary association for shopping
    public Association createTempAsso(int item_Id){
        return new Association(item_Id);
    }
//get combined assos as ArrayList
    public ArrayList<Association> getCombinedAssos(){
        return db.getAllAssos();
    }

    public ArrayList<Association> findNotifiedAssos(ArrayList<Item> notifiedItems){
        ArrayList<Association> allAssos = new ArrayList<>(getCombinedAssos());
        ArrayList<Association> foundAssos = new ArrayList<>();
        boolean found;

        for(Item item : notifiedItems){
            found = false;
            for(Association asso : allAssos){
                if(item.getItem_id()==asso.getItem_Id()){
                    foundAssos.add(asso);
                    found = true;
                }
            }
            if(!found){
                foundAssos.add(createTempAsso(item.getItem_id()));
            }
        }
        return foundAssos;
    }

    public HashMap<Integer, ArrayList<Association>> findShoppingAssos(ArrayList<ItemList> lists, ArrayList<Item> notifiedItems) {
        HashMap<Integer,ArrayList<Association>> listMap = new HashMap<>();
        if(notifiedItems.size()==0){
            return listMap;
        }else if(lists.size()!= 0){
            ArrayList <Association> notifiedAssos =  findNotifiedAssos(notifiedItems);

            for(Association currAsso: notifiedAssos) {
                for(ItemList currList:lists){

                    if(currAsso.getList_Id()==0){//check if item is wild(part of all lists)

                        if(listMap.containsKey(currList.getList_Id())){//check if listmap already has that list into it
                            listMap.get(currList.getList_Id()).add(currAsso);
                        }else{
                            ArrayList<Association> temp = new ArrayList<>();
                            temp.add(currAsso);
                            listMap.put(currList.getList_Id(),temp);
                        }
                    }else if(currList.getList_Id() == currAsso.getList_Id()){

                        if(listMap.containsKey(currList.getList_Id())){//check if listmap already has that list into it
                            listMap.get(currList.getList_Id()).add(currAsso);
                        }else{
                            ArrayList<Association> temp = new ArrayList<>();
                            temp.add(currAsso);
                            listMap.put(currList.getList_Id(),temp);
                        }
                    }
                }
            }

        }else{ // creates a list of items if there are not lists but items have run out
            ItemList allLists = new ItemList("all","every");
            allLists.setList_Id(0);
            ArrayList<Association> aForAllLists = new ArrayList<>();
            for(Item curr: notifiedItems){
                aForAllLists.add(createTempAsso(curr.getItem_id()));
            }
            listMap.put(allLists.getList_Id(),aForAllLists);
            lists.add(allLists);
        }
        return listMap;
    }
    public void addToSave(Association asso){
        db.addSingeAsso(asso);
    }

//remove association that are part of every list (get first to save)
    public void removeWildAsso(Association currentAsso, HashMap<Integer, ArrayList<Association>> shoppingAssos) {
        Set<Map.Entry<Integer, ArrayList<Association>>> keys = shoppingAssos.entrySet();
        Iterator<Map.Entry<Integer,ArrayList<Association>>> iterator = keys.iterator();
        boolean first = true;
        while(iterator.hasNext()){
            Map.Entry<Integer, ArrayList<Association>> current = iterator.next();
            ArrayList<Association> currAssos = current.getValue();
            for(int i =0;i< currAssos.size();i++){
                if(currAssos.get(i).getItem_Id() == currentAsso.getItem_Id()){
                    if(first) {
                        Association asso = currAssos.get(i);
                        asso.setBought(true);
                        asso.setDeleteFlag(true);
                        addToSave(asso);
                    }
                    currAssos.remove(i);
                    i--;
                    first=false;

                }
            }
        }
    }
//remove association that are been bought
    public void removeBoughtAsso(Association currentAsso, HashMap<Integer, ArrayList<Association>> shoppingAssos){

        Set<Map.Entry<Integer, ArrayList<Association>>> keys = shoppingAssos.entrySet();
        for (Map.Entry<Integer, ArrayList<Association>> current : keys) {
            ArrayList<Association> currAssos = current.getValue();
            for (int i = 0; i < currAssos.size(); i++) {
                if (currAssos.get(i).getItem_Id() == currentAsso.getItem_Id() &&
                        currAssos.get(i).getList_Id() == currentAsso.getList_Id()) {

                    Association asso = currAssos.get(i);
                    Association boughtAsso = new Association(asso.getList_Id(),asso.getItem_Id(),asso.getQuantity());
                    boughtAsso.setBought(true);
                    boughtAsso.setDeleteFlag(true);
                    addToSave(boughtAsso);

                    currAssos.remove(i);
                    i--;
                } else if (currAssos.get(i).getItem_Id() == currentAsso.getItem_Id()) {
                    currAssos.remove(i);
                    i--;
                }
            }
        }
    }
    public void insertOnFlyItemAsso(Association asso, HashMap<Integer, ArrayList<Association>> shoppingAssos) {
        for(ArrayList<Association> curAssoArray:shoppingAssos.values()){
            curAssoArray.add(asso);
        }
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

}
