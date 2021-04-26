package com.ignas.android.groceryshoppingapp.View.Layer.Item;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.AssoResources;
import com.ignas.android.groceryshoppingapp.Models.Association;

import java.util.ArrayList;
import java.util.HashMap;

public class AssoViewModel extends ViewModel {
    private final AssoResources assoResources;


    private final  HashMap<Integer,ArrayList<Association>> allAssociations = new HashMap<>();
    private final MutableLiveData<ArrayList <Association>> currentLive = new MutableLiveData<>();


    public AssoViewModel(){
        assoResources = new AssoResources();
    }

    //gets Associations if found  by list_id (filtering)
    public ArrayList<Association> setAsso(int list_Id){
        ArrayList<Association> newAsso = new ArrayList<>();
        if(allAssociations.containsKey(list_Id)){
            currentLive.setValue(allAssociations.get(list_Id));
        }else{
                allAssociations.put(list_Id,newAsso);
                currentLive.setValue(newAsso);
        }
        return currentLive.getValue();
    }
    //add new Association
    public void addAsso(int list_Id,int item_Id,int quantity){
        Association a = new Association(list_Id,item_Id,quantity);
        ArrayList<Association> current = setAsso(list_Id);
        current.add(a);
        currentLive.setValue(current);
    }
    public boolean deleteAsso(int item_Id){
        ArrayList<Association> temp = currentLive.getValue();
        Association result = temp.stream()
                .filter(a-> a.getItem_Id() == item_Id)
                .findFirst().orElse(null);
        if(result!=null){
            temp.remove(result);
            return true;
        }
        return false;
    }

    public LiveData<ArrayList<Association>> getCurrentLive() {
        return currentLive;
    }
    public ArrayList<Association> getCurrentAsso(){return currentLive.getValue();}
}
