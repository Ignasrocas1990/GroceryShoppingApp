package com.ignas.android.groceryshoppingapp.Models;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
/***
 * Author:Ignas Rocas
 * Student Id: C00135830
 * Date: 28/05/2021
 * Purpose: Project, Association model
 */
public class Association extends RealmObject {

    @PrimaryKey
    private int asso_Id;
    {
        Random r = new Random();
        asso_Id = r.nextInt();
    }

    private int list_Id=0;
    private int item_Id;
    private int quantity=0;
    private boolean deleteFlag = false;
    private boolean bought = false;
    private Date boughtDate;
    private String displayDate="";

    //constructor
    public Association(){}

    public Association(int list_Id, int item_Id, int quantity) {
        this.list_Id = list_Id;
        this.item_Id = item_Id;
        this.quantity = quantity;
    }

    public Association(int item_Id) {
        this.item_Id = item_Id;
    }

    public Association(int item_id, int amount) {
        this.item_Id = item_id;
        this.quantity = amount;
    }

    //getters & setters

    public void setAsso_Id(int asso_Id) {
        this.asso_Id = asso_Id;
    }

    public boolean isBought() {
        return bought;
    }
    public void setBought(boolean bought) {
        this.bought = bought;
        Calendar cal = Calendar.getInstance();
        boughtDate = cal.getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("d/M/yyyy k:m");
        displayDate = formatter.format(boughtDate);
    }

    public String getDisplayDate() {
        return displayDate;
    }

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public int getAsso_Id() {
        return asso_Id;
    }

    public int getList_Id() {
        return list_Id;
    }

    public void setList_Id(int list_Id) {
        this.list_Id = list_Id;
    }

    public int getItem_Id() {
        return item_Id;
    }

    public void setItem_Id(int item_Id) {
        this.item_Id = item_Id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getBoughtDate() {
        return boughtDate;
    }
}
