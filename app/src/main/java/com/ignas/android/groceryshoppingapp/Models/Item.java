package com.ignas.android.groceryshoppingapp.Models;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {


    @PrimaryKey
    private int item_Id;
    {
        Random r = new Random();
        item_Id = r.nextInt();
        setRunOutDate(7);
    }
    private String itemName="";
    private float price=0.f;
    private Date runOutDate;
    private int lastingDays=0;
    private boolean deleteFlag = false;
    private boolean notified = false;

//constructors
    public Item(){}
    public Item(String itemName) {
        this.itemName = itemName;
    }
    public Item(int now){ lastingDays = now;}
    public Item(String name,int item_Id,int lastingDays){
        setItemName(name);
        setLastingDays(lastingDays);
        this.item_Id = item_Id;
    }

    public Item(String itemName, float price, int lastingDays) {
        this.itemName = itemName;
        this.price = price;
        this.lastingDays = lastingDays;
        setRunOutDate(lastingDays);
        
    }
//getters & setters

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public int getItem_id() {
        return item_Id;
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

    public Date getRunOutDate() {
        return runOutDate;
    }

    public void setRunOutDate(int lastingDays) {
        lastingDays-=2;
        Calendar calendar = Calendar.getInstance();
        if(lastingDays != 0){
            calendar.add(Calendar.MILLISECOND,lastingDays*1000);//TODO ------testing (need to be changed)
        }else{
            calendar.add(Calendar.MILLISECOND,10*1000);//TODO --  default
        }
        this.runOutDate = calendar.getTime();
    }
    public int getLastingDays() {
        return lastingDays;
    }

    public void setLastingDays(int lastingDays) {
        this.lastingDays = lastingDays;
        setRunOutDate(lastingDays);
    }
}
