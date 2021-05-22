package com.ignas.android.groceryshoppingapp.Logic;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Service.Repository;

public class DateViewModel extends ViewModel {

    private final MutableLiveData<Boolean> app_switch = new MutableLiveData<>();
    private final MutableLiveData<Float> liveTotal = new MutableLiveData<>();
    private final Repository repository;


    public DateViewModel(){
        repository = Repository.getInstance();
        app_switch.setValue(repository.getDBSwitch());
    }
//getters & setters
    public boolean getSwitch() {
        return app_switch.getValue();
    }

    public void setTotal(float price) {
        liveTotal.setValue(price);
    }
    public void setSwitch(boolean state) {
        app_switch.setValue(state);
    }
    public boolean updateSwitch(){
        boolean dbSwitch = repository.getDBSwitch();
        if(dbSwitch != app_switch.getValue()){
            repository.updateSwitch(app_switch.getValue());
        }
        if(!dbSwitch && app_switch.getValue()){
            return true;
        }
        return false;
    }


//total methods for shopping day
    public void addToTotal(float price){
        try{
            liveTotal.setValue(liveTotal.getValue()+price);
        }catch(NullPointerException e){
            Log.i("log", "addToTotal: "+e.getMessage());
        }
    }

//live observational methods
    public LiveData<Boolean> getLiveSwitch(){return app_switch;}
    public LiveData<Float> getLiveTotal() {
        return liveTotal;
    }

}
