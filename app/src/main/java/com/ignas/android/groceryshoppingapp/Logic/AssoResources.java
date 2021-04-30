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
    private final HashMap<Integer,ArrayList<Association>> app_assos = new HashMap<>();

    private final ArrayList<Association> db_assos;

    private final HashMap<Integer,ArrayList<Association>>  toSave = new HashMap<>();
    private final ArrayList<Association> toDelete = new ArrayList<>();

    public AssoResources() {
        db = new RealmDb();
        db_assos = db.getAllAssos();
        list_to_map();

    }
    public HashMap<Integer, ArrayList<Association>> getApp_assos() {
        return app_assos;
    }


    public void list_to_map(){

        ArrayList<Association> temp;
        int key_Id;
        for(Association asso : db_assos){
            key_Id = asso.getList_Id();
            if(app_assos.containsKey(key_Id)){

               temp =  app_assos.get(key_Id);
                temp.add(asso);

            }else{
                temp = new ArrayList<>();
                temp.add(asso);
                app_assos.put(key_Id, temp);
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
            templates = toSave.get(list_Id);
            templates.add(newAsso);
        }
        current.add(newAsso);
        return current;
    }

//delete single association method from current live list of associations
    public ArrayList<Association> deleteAsso(int item_Id, ArrayList<Association> curr){

//find selected association
        Association asso = curr.stream()
                .filter(a-> a.getItem_Id() == item_Id)
                .findFirst().orElse(null);

        if(asso!=null) { curr.remove(asso);
            ArrayList<Association> currentSaved = toSave.get(asso.getList_Id());

            if(currentSaved == null){
                toDelete.add(asso);
                return curr;
            }

//its an old object(been modified) -deleting-
            if(Check.assoEqual(db_assos,asso) && Check.assoEqual(currentSaved,asso)){
                currentSaved.remove(asso);
                toDelete.add(asso);

                db_assos.remove(asso);
//old object not modified -deleting-
            }else if(Check.assoEqual(db_assos,asso)){

                toDelete.add(asso);
                db_assos.remove(asso);

//new object just created -deleting-
            }else { currentSaved.remove(asso); }
        }
        return curr;
    }

//set multiple items to be deleted from one list
    public void severList(ArrayList<Association> listToDelete){
        if(listToDelete.size() !=0){

                for(int i=0;i<listToDelete.size();i++){

                deleteAsso(listToDelete.get(i).getItem_Id(),listToDelete);
                i--;
                /*
                if(toSave.containsKey(curr.getList_Id())){
                    ArrayList<Association> toSaveTemp = toSave.get(curr.getList_Id());

                    Association result = toSaveTemp.stream()
                            .filter(asso->asso.getItem_Id()==curr.getList_Id())
                            .findFirst().orElse(null);
                    if(result!=null){
                        toSaveTemp.remove(result);

                    }
                }else{
                    toDelete.add(curr);
                    listToDelete.remove(curr);
                }

                 */
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

//db methods
    public void updateDB_Associations(){
            ArrayList<Association> fullSave = new ArrayList<>();
            for(ArrayList<Association> a: toSave.values()){
                fullSave.addAll(a);
            }

            if(toSave.size()>1){
                db.addMultipleAsso(fullSave);
            }else if (toSave.size() != 0){
                db.addSingeAsso(fullSave.get(0));
            }
            if(toDelete.size()>1){
                db.removeAssos(toDelete);
            }else if(toDelete.size()==1){
                db.removeAsso(toDelete.get(0));
            }
    }
}
