package com.ignas.android.groceryshoppingapp.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class AlarmSwitch extends RealmObject {

    @PrimaryKey
    private int switch_Id = 1;
    private boolean deleteFlag = false;

    private boolean switched;

    public AlarmSwitch(){}

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

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
