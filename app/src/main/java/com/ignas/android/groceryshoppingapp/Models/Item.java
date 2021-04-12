package com.ignas.android.groceryshoppingapp.Models;

import org.bson.types.ObjectId;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject{

    //ObjectId item_id = new ObjectId(Integer.toString(new Random().nextInt()));
    @PrimaryKey
    static String item_id;
    {
        Random r = new Random();
        item_id = Integer.toHexString(r.nextInt());
    }

    private String itemName="";
    private float price=0;
    private int amount=0;
    private Date runOutDate=null;
    private int lastingDays =0;

    public Item(){}
    public Item(String itemName) {
        this.itemName = itemName; }

    public Item(String itemName, float price, int amount, int lastingDays) {
        this.itemName = itemName;
        this.price = price;
        this.amount = amount;
        this.lastingDays = lastingDays;
    }

    public static String getItem_id() {
        return item_id;
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
