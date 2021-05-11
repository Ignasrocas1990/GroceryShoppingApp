package com.ignas.android.groceryshoppingapp.Models;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Report extends RealmObject {
    @PrimaryKey
    private int report_Id;
    private Date reportDate;
    RealmList<Integer> item_Ids;
    private float total = 0.f;


    public Report() {
        item_Ids = new RealmList<>();
        setReport_Id();
        setReportDate();
    }
    //getters & setters

    public int getReport_Id() {
        return report_Id;
    }

    private void setReport_Id() {
        Random r = new Random();
        report_Id = r.nextInt();
    }

    public Date getReportDate() {
        return reportDate;
    }

    private void setReportDate() {
        Calendar cal = Calendar.getInstance();
        this.reportDate = cal.getTime();
    }

    public RealmList<Integer> getBoughtItems() {
        return item_Ids;
    }
    public void addItemId(int item_Id){
        this.item_Ids.add(item_Id);
    }
    public void setItems(ArrayList<Integer>item_Ids){
        this.item_Ids.addAll(item_Ids);
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
