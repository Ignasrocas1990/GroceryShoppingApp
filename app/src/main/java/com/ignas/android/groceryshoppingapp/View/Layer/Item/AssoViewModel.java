package com.ignas.android.groceryshoppingapp.View.Layer.Item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.AssoResources;
import com.ignas.android.groceryshoppingapp.Models.Association;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeSet;

public class AssoViewModel extends ViewModel {
    private MutableLiveData<ArrayList<Association>> associations = new MutableLiveData<>();

    private HashMap<Integer,ArrayList<Association>> beenSelected = new HashMap<>();

    private final AssoResources assoResources;
    private ArrayList<Association> current=null;

    public AssoViewModel(){
        assoResources = new AssoResources();
        associations.setValue(assoResources.getAssos());
    }

    //gets Associations if found  by list_id (filtering)
    public ArrayList<Association> getAsso(int list_Id){ // the same as set Association

        if(beenSelected.containsKey(list_Id)){
            this.current=beenSelected.get(list_Id);
        }else{
            this.current = new ArrayList<>();
            beenSelected.put(list_Id,current);
        }
        return current;
    }

    public ArrayList<Association> getCurAsso(){return current;}

    //add new Association
    public void addAsso(int list_Id,int item_Id,int quantity){

        Association a = new Association(list_Id,item_Id,quantity);
        ArrayList<Association> copy = associations.getValue();
        current=getAsso(list_Id);
        current.add(a);

        copy.add(a);
        associations.setValue(copy);
    }
    public void deleteAsso(int list_Id,int item_Id){
        //------TODO---------------------------

    }
    public ArrayList<Association> getAllAssos(){
        return associations.getValue();
    }


    public LiveData<ArrayList<Association>> getLiveAssos() {
        return associations;
    }
}
