package com.ignas.android.groceryshoppingapp.Logic;


import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Models.Report;
import com.ignas.android.groceryshoppingapp.Models.ShoppingItem;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;
import java.util.Random;

public class DateResources {
    private final RealmDb db;
    private final boolean dbSwitch;


    public DateResources() {
        db = new RealmDb();
        dbSwitch = db.getSwitch();
    }
    public ArrayList<Report> getReports(){
        return db.getReports();
    }

//notification switch methods
    public boolean getDBSwitch(){
        return dbSwitch;
    }

    public Boolean updateSwitch(boolean appSwitch){

        if(appSwitch != dbSwitch){
            db.setSwitch(appSwitch);
        }
        return appSwitch && !dbSwitch;
    }
    public ShoppingItem addSPItem(String name,int amount,float price){

        Random r = new Random();
        return new ShoppingItem(r.nextInt(),name, amount, price);
    }
    public void createReport(float total, ArrayList<ShoppingItem> items){
        Report report = new Report();
        ArrayList<Integer> item_Ids = new ArrayList<Integer>();
        for(ShoppingItem i : items){
            item_Ids.add(i.getItem_Id());
        }
        report.setTotal(total);
        report.setItems(item_Ids);
        db.addReport(report);
    }
}
