package com.ignas.android.groceryshoppingapp.Logic;

import com.ignas.android.groceryshoppingapp.Models.AlarmSwitch;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

public class DateResources {
    private final RealmDb db;
    final private boolean dbSwitch;

    public DateResources() {
        db = new RealmDb();
        dbSwitch = getDBSwitch();
    }
    public boolean getDBSwitch(){
        return db.getSwitch();
    }

    public void updateSwitch(boolean appSwitch){

        if(appSwitch != dbSwitch){
            db.setSwitch(appSwitch);
        }
    }


}
