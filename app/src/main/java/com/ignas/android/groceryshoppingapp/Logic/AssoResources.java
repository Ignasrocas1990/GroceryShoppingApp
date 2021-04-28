package com.ignas.android.groceryshoppingapp.Logic;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;
import java.util.HashMap;

public class AssoResources {

    private final RealmDb db;
    private final HashMap<Integer,ArrayList<Association>> allAssociations = new HashMap<>();

    private final HashMap<Integer,ArrayList<Association>>  toSave = new HashMap<>();
    private final ArrayList<Association> toDelete = new ArrayList<>();

    public AssoResources() {
        db = new RealmDb();
        list_to_map(db.getAllAssos());
    }
    public HashMap<Integer, ArrayList<Association>> getAllAssociations() {
        return allAssociations;
    }


    public void list_to_map(ArrayList<Association> db_list){
        for(Association asso :db_list){
            int key_Id = asso.getList_Id();
            if(allAssociations.containsKey(key_Id)){
                allAssociations.get(key_Id).add(asso);
            }else{
                allAssociations.put(key_Id, new ArrayList<>());
            }
        }
    }


//gets Associations if found  by list_id (filtering)
    public ArrayList<Association> getAsso(int list_Id){

        ArrayList<Association> newAsso = new ArrayList<>();
        if(allAssociations.containsKey(list_Id)){
            newAsso = allAssociations.get(list_Id);
        }else{
            allAssociations.put(list_Id,newAsso);
        }
        return newAsso;
    }

    //add new Association
    public ArrayList<Association> addAsso(int list_Id,int item_Id,int quantity){
        Association a = new Association(list_Id,item_Id,quantity);
        ArrayList<Association> current = getAsso(list_Id);
        if(toSave.containsKey(list_Id)){
            toSave.get(list_Id).add(a);
        }else{
            ArrayList<Association> temp =new ArrayList<>();
            temp.add(a);
            toSave.put(list_Id,temp);
        }
        current.add(a);

        return current;
    }

    //delete single association method
    public ArrayList<Association> deleteAsso(int item_Id, ArrayList<Association> curr){
        Association result = curr.stream()
                .filter(a-> a.getItem_Id() == item_Id)
                .findFirst().orElse(null);

        if(result!=null){
            if(toSave.containsKey(result.getList_Id())){                            //check if item is added into save map
                ArrayList<Association> toSaveTemp = toSave.get(result.getList_Id());

                Association toSaveResult = toSaveTemp.stream()
                        .filter(a->a.getItem_Id()==result.getList_Id())
                        .findFirst().orElse(null);
                if(toSaveResult!=null){
                    toSaveTemp.remove(toSaveResult);
                }
            }else{
                toDelete.add(result);
            }
            curr.remove(result);
        }
        return curr;
    }
    //set multiple items to be deleted
    public void severList(ArrayList<Association> listToDelete){
        for(Association curr : listToDelete){
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
        }

    }
    public ArrayList<Association> findItemAssos(int item_Id){
        Association curAsso;
        ArrayList<Association> foundAssos = new ArrayList<>();
        for(ArrayList<Association> assoIndexArray : allAssociations.values()){
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


//db methods
    public void saveAssociations(){
            ArrayList<Association> temp = new ArrayList<>();
            for(ArrayList<Association> list : toSave.values()){
                temp.addAll(list);
            }
            if(temp.size()>1){
                db.addMultipleAsso(temp);
            }else if (temp.size() != 0){
                db.addSingeAsso(temp.get(0));
            }
    }
    public void deleteAssociations(){
        for(Association asso : toDelete){
            db.removeAsso(asso);
        }
    }
}
