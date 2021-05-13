package com.ignas.android.groceryshoppingapp.Logic;

import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;
import java.util.HashMap;

public class AssoResources {

    private final RealmDb db;
    private final HashMap<Integer,ArrayList<Association>> app_assos = new HashMap<>();//contains list_Id as key
    private final HashMap<Integer,Association> db_assos = new HashMap<>();//contains asso_Id as key
    private final HashMap<Integer,ArrayList<Association>>  toSave = new HashMap<>();//list id as key

    //private final ArrayList<Association> toDelete = new ArrayList<>();

    public AssoResources() {
        db = new RealmDb();
        list_to_map(db.getAllAssos());

    }
    public HashMap<Integer, ArrayList<Association>> getApp_assos() {
        return app_assos;
    }

//converts db association to a map.
    public void list_to_map(ArrayList<Association> dbAssos){
        ArrayList<Association> temp;
        int list_Id;
        for(Association asso : dbAssos){
            db_assos.put(asso.getAsso_Id(),asso);

            list_Id = asso.getList_Id();
            if(app_assos.containsKey(list_Id)){
               temp =  app_assos.get(list_Id);
                temp.add(asso);

            }else{
                temp = new ArrayList<>();
                temp.add(asso);
                app_assos.put(list_Id, temp);
            }
        }
    }

//gets Associations if found  by list_id (filtering)
    public ArrayList<Association> getAsso(int list_Id){

        ArrayList<Association> newAsso = new ArrayList<>();
        if(app_assos.containsKey(list_Id)){
            newAsso = app_assos.get(list_Id);
        }else{
            app_assos.put(list_Id,newAsso);
        }
        return newAsso;
    }

    //add new Association
    public ArrayList<Association> addAsso(int list_Id, int item_Id, int quantity, ArrayList<Association> current){
        Association newAsso = new Association(list_Id, item_Id, quantity);
        ArrayList<Association> templates;
//check if list of associations exist in saved map
        if(!toSave.containsKey(list_Id)){
             templates = new ArrayList<>();
            templates.add(newAsso);
            toSave.put(list_Id,templates);
        }else{
            if(list_Id==0){
                templates = new ArrayList<>();
            }else{
                templates = toSave.get(list_Id);
            }
            templates.add(newAsso);
        }
        if(current==null){
            current = new ArrayList<>();
        }
        current.add(newAsso);
        return current;
    }

//delete single association method from current live list of associations
    public ArrayList<Association> deleteAsso(int item_Id, ArrayList<Association> curr) {
        ArrayList<Association> currentSaved=null;
//find selected association
        Association asso = curr.stream()
                .filter(a -> a.getItem_Id() == item_Id)
                .findFirst().orElse(null);


        if (asso != null) {
            curr.remove(asso);
            currentSaved = toSave.get(asso.getList_Id());

            if (db_assos.containsKey(asso.getAsso_Id())) {//old item
                if (currentSaved != null) {
                    asso.setDeleteFlag(true);
                    return curr;
                }
            } else {
                if(currentSaved !=null){
                    currentSaved.remove(asso);
                }

            }
        }
        return curr;

    }
//set multiple items to be deleted from one list
    public void severList(ArrayList<Association> listToDelete){
        if(listToDelete.size() !=0){

                for(int i=0;i<listToDelete.size();i++){

                deleteAsso(listToDelete.get(i).getItem_Id(),listToDelete);
                i--;
            }
        }
    }

    public ArrayList<Association> findItemAssos(int item_Id){
        Association curAsso;
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
        return foundAssos;
    }

// remove item associations from list/s
    public void severItem(ArrayList<ItemList> removed, Item deleteItem){
        int list_id;
        ArrayList<Association> currentAsso;
            for(int i = 0;i<removed.size();i++){

             list_id = removed.get(i).getList_Id();
// check if each list id is here.
            if(app_assos.containsKey(list_id)){
                currentAsso = app_assos.get(list_id); // get Array of association for list_Id

                deleteAsso(deleteItem.getItem_id(),currentAsso);

            }
        }
    }

//saves,deletes from database
    public void updateDB_Associations(){
            ArrayList<Association> fullSave = new ArrayList<>();
            for(ArrayList<Association> a: toSave.values()){
                fullSave.addAll(a);
            }
            if(fullSave.size()>1){
                db.addMultipleAsso(fullSave);
            }else if (fullSave.size() != 0){
                db.addSingeAsso(fullSave.get(0));
            }
    }
//find item associations to list for each item
    public ArrayList<Association> findAssociations(ArrayList<Item> items) {
        ArrayList<Association> associations = new ArrayList<>();
        for(Item item : items){
            associations.addAll(findItemAssos(item.getItem_id()));
        }
        return associations;
    }
//find association by id
    public Association findAssoById(int asso_Id){
        if(db_assos.containsKey(asso_Id)){
            return db_assos.get(asso_Id);
        }
        return null;
    }
}
