package com.ignas.android.groceryshoppingapp.Models;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
/***
 * Author:Ignas Rocas
 * Student Id: C00135830
 * Date: 28/05/2021
 * Purpose: Project, Item Model
 */
public class Item extends RealmObject {

    @PrimaryKey
    private int item_Id;

    private String itemName="";
    private float price=0.f;
    private Date runOutDate;
    private int lastingDays=0;
    private boolean deleteFlag = false;
    private boolean notified = true;
    @NonNls
    private String displayText;

    //constructors
    {
        Random r = new Random();
        item_Id = r.nextInt();
        setRunOutDate(7);
    }

    public Item(){}
    public Item(int now){ lastingDays = now;}
    public Item(String name) {
        setItemName(name);
    }
    public Item(String name,int item_Id,int lastingDays){
        setItemName(name);
        setLastingDays(lastingDays);
        this.item_Id = item_Id;
    }
    public Item(String name, float price, int lastingDays) {
        setItemName(name);
        this.price = price;
        this.lastingDays = lastingDays;
        setRunOutDate(lastingDays);
    }
    public Item(String displayName,int id){
        displayText = displayName;
        item_Id = id;
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
        defaultDisplayString(itemName);
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

//important for notification scheduling
    public void setRunOutDate(int lastingDays) {
        Calendar calendar = Calendar.getInstance();

        if(lastingDays >=4) lastingDays-=2;
        else if(lastingDays >=2) lastingDays-=1;

        if(lastingDays != 0){
            //calendar.add(Calendar.DAY_OF_WEEK,lastingDays);//TODO for chris suggestion un-comment for normal use
            calendar.add(Calendar.MILLISECOND,lastingDays*1000);//TODO for chris suggestion comment for normal use
        }else{
            //calendar.add(Calendar.DAY_OF_WEEK,999);//TODO for chris suggestion un-comment for normal use
            calendar.add(Calendar.MILLISECOND,10*1000);//TODO for chris suggestion comment for normal use
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

    private void defaultDisplayString(String itemName){
        this.displayText = "Item: "+itemName;
    }

    @NotNull
    @Override
    public String toString() {
        return displayText;
    }
}
