package com.ignas.android.groceryshoppingapp.Models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
/***
 * Author:Ignas Rocas
 * Student Id: C00135830
 * Date: 28/05/2021
 * Purpose: Project, Alarm Switch model
 */
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
