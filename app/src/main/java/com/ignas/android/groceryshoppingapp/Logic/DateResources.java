package com.ignas.android.groceryshoppingapp.Logic;

import com.ignas.android.groceryshoppingapp.Models.AlarmSwitch;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.Date;

public class DateResources {
    private final RealmDb db;
    private final boolean dbSwitch;


    public DateResources() {
        db = new RealmDb();
        dbSwitch = db.getSwitch();
    }

//notification switch methods
    public boolean getDBSwitch(){
        return dbSwitch;
    }

    public Boolean updateSwitch(boolean appSwitch){

        if(appSwitch != dbSwitch){
            db.setSwitch(appSwitch);
        }
        if(appSwitch && !dbSwitch){
            return true;
        }
        return false;
    }


}