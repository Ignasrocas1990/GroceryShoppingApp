package com.ignas.android.groceryshoppingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject {

    //ObjectId item_id = new ObjectId(Integer.toString(new Random().nextInt()));

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
    private boolean running = false;

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

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
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
    //for comparing objects
    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Item i = (Item) obj;
        return(itemName.equals(i.getItemName()) && Float.compare(price,i.getPrice())==0
                 && (runOutDate.compareTo(i.getRunOutDate())==0)
                && lastingDays == i.getLastingDays() && running == i.isRunning()) ;
    }
}
