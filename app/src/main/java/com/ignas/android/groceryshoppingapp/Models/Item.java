package com.ignas.android.groceryshoppingapp.Models;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Item extends RealmObject{
    @PrimaryKey
    private static int itemID=0;

    private String itemName="";
    private float price=0;
    private int amount=0;
    private Date runOutDate=null;
    private int lastingDays =0;

    {
        itemID++;
    }

    public Item(){}
    public Item(String itemName) {
        this.itemName = itemName; }

    public Item(String itemName, float price, int amount, int lastingDays) {
        this.itemName = itemName;
        this.price = price;
        this.amount = amount;
        this.lastingDays = lastingDays;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getRunOutDate() {
        return runOutDate;
    }

    public void setRunOutDate() {
        int days = getLastingDays();
        if(days != 0){
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_WEEK,days);
            runOutDate = calendar.getTime();
        }


    }

    public int getLastingDays() {
        return lastingDays;
    }

    public void setLastingDays(int lastingDays) {
        this.lastingDays = lastingDays;
    }

}
