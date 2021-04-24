package com.ignas.android.groceryshoppingapp.View.Layer.Item;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.AssoResources;
import com.ignas.android.groceryshoppingapp.Models.Association;

import java.util.ArrayList;
import java.util.HashMap;

public class AssoViewModel extends ViewModel {
    private final AssoResources assoResources;

    private final MutableLiveData<ArrayList<Association>> AllAssociations = new MutableLiveData<>();
    private final HashMap<Integer,ArrayList<Association>> beenSelected = new HashMap<>();

    private ArrayList<Association> current=null;
    private final MutableLiveData<ArrayList <Association>> currentLive = new MutableLiveData<>();


    public AssoViewModel(){
        assoResources = new AssoResources();
        AllAssociations.setValue(assoResources.getAssos());
    }

    //gets Associations if found  by list_id (filtering)
    public ArrayList<Association> setAsso(int list_Id){
        if(list_Id == -1){
            this.current = null;
        }else if(beenSelected.containsKey(list_Id)){
            this.current=beenSelected.get(list_Id);
        }else{
            this.current = new ArrayList<>();
            beenSelected.put(list_Id,current);
            currentLive.setValue(current);
        }
        return current;
    }

    public ArrayList<Association> getCurAsso(){return current;}

    //add new Association
    public void addAsso(int list_Id,int item_Id,int quantity){

        Association a = new Association(list_Id,item_Id,quantity);
        ArrayList<Association> copy = AllAssociations.getValue();
        current=setAsso(list_Id);
        current.add(a);

        copy.add(a);
        AllAssociations.setValue(copy);
    }
    public void deleteAsso(int list_Id,int item_Id){
        //TODO---------------------------

    }
    public ArrayList<Association> getAllAssos(){
        return AllAssociations.getValue();
    }

    public LiveData<ArrayList<Association>> getLiveAssos() {
        return AllAssociations;
    }
    public LiveData<ArrayList<Association>> getCurrentLive() {
        return currentLive;
    }
}
