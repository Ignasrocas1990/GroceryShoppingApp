package com.ignas.android.groceryshoppingapp.View.Layer.ShoppingDate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.DateResources;

public class DateViewModel extends ViewModel {

    private MutableLiveData<Boolean> app_switch = new MutableLiveData<>();
    private DateResources dateResources;

    public DateViewModel(){
        dateResources = new DateResources();
        app_switch.setValue(dateResources.getDBSwitch());
    }

    public boolean getSwitch() {
        return app_switch.getValue();
    }

    public void setSwitch(boolean state) {
        app_switch.setValue(state);
    }
//update switch database
    public void updateSwitch(){
        dateResources.updateSwitch(app_switch.getValue());
    }
//live observational methods
    public LiveData<Boolean> getLiveSwitch(){return app_switch;}
}
