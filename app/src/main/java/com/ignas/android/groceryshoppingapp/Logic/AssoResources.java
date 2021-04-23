package com.ignas.android.groceryshoppingapp.Logic;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;

public class AssoResources {

    private final RealmDb db;
    ArrayList<Association> db_list;


    public AssoResources() {
        db = new RealmDb();
        db_list = db.getAllAssos();
    }
    public ArrayList<Association> getAssos(){
        return db_list;
    }


}
