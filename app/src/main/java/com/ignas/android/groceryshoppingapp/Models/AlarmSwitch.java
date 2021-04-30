package com.ignas.android.groceryshoppingapp.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AlarmSwitch extends RealmObject {

    @PrimaryKey
    private int switch_Id = 1;

    private boolean switched;

    public AlarmSwitch(){}

    public int getId() {
        return switch_Id;
    }

    public boolean isSwitched() {
        return switched;
    }

    public void setSwitch(boolean switched) {
        this.switched = switched;
    }
}
