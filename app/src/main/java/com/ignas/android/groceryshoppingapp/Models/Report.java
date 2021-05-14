package com.ignas.android.groceryshoppingapp.Models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
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
    private String dateString;
    RealmList<Integer> asso_Ids;
    private float total = 0.f;


    public Report() {
        asso_Ids = new RealmList<>();
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(" dd/MM/yyyy k:m:s  ");
        dateString = formatter.format(reportDate);
    }
    public void setEmptyDateString(String text){
        dateString = text;
    }

    public ArrayList<Integer> getBoughtAssos() {
        ArrayList<Integer> temp = new ArrayList<>();
        temp.addAll(asso_Ids);
        return temp;
    }
    public void setAssos(ArrayList<Integer>asso_Ids){
        this.asso_Ids.addAll(asso_Ids);
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return dateString;
    }
}
