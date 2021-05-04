package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.DateResources;

import java.util.Date;

public class DateViewModel extends ViewModel {


    private final MutableLiveData<Boolean> app_switch = new MutableLiveData<>();
    private final DateResources dateResources;

    public DateViewModel(){
        dateResources = new DateResources();
        app_switch.setValue(dateResources.getDBSwitch());
    }

//notification switch methods
    public boolean getSwitch() {
        return app_switch.getValue();
    }
    public void setSwitch(boolean state) {
        app_switch.setValue(state);
    }
    public boolean updateSwitch(){
        return dateResources.updateSwitch(app_switch.getValue());
    }


//live observational methods
    public LiveData<Boolean> getLiveSwitch(){return app_switch;}
}
