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

public class Item extends RealmObject implements Parcelable {

    //ObjectId item_id = new ObjectId(Integer.toString(new Random().nextInt()));

    @PrimaryKey
    private int item_Id;
    {
        Random r = new Random();
        item_Id = r.nextInt();
        setRunOutDate(7);

    }

    private String itemName="";
    private float price=0;
    private int amount=0;
    private Date runOutDate;
    private int lastingDays=0;
    private boolean running = false;
    private RealmList<Integer> list_Ids = new RealmList<Integer>();

//constructors
    public Item(){}
    public Item(String itemName) {
        this.itemName = itemName;
    }

    public Item(String itemName, float price, int amount, int lastingDays) {
        this.itemName = itemName;
        this.price = price;
        this.amount = amount;
        this.lastingDays = lastingDays;
        setRunOutDate(lastingDays);
        
    }

//getters & setters
    public RealmList<Integer> getList_Ids() {
        return list_Ids;
    }

    public void setList_Ids(RealmList<Integer> list_Ids) {
        this.list_Ids = list_Ids;
    }

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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getRunOutDate() {
        return runOutDate;
    }

    public void setRunOutDate(int lastingDays) {
        Calendar calendar = Calendar.getInstance();
        if(lastingDays != 0){
            calendar.add(Calendar.MILLISECOND,lastingDays*1000);//TODO ------testing (need to be changed)
        }else{
            calendar.add(Calendar.MILLISECOND,10);
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
        if (list_Ids.size() != i.getList_Ids().size()) return false;
        if(itemName.equals(i.getItemName()) && Float.compare(price,i.getPrice())==0
                && amount == i.getAmount() && (runOutDate.compareTo(i.getRunOutDate())==0)
                && lastingDays == i.getLastingDays() && running == i.isRunning()) {

            if(list_Ids.equals(i.getList_Ids())) return true;
            Collections.sort(list_Ids);
            Collections.sort(i.getList_Ids());
            return list_Ids.equals(i.getList_Ids());

        }
        return false;
    }

//Parcelable methods - auto generated
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.item_Id);
        dest.writeString(this.itemName);
        dest.writeFloat(this.price);
        dest.writeInt(this.amount);
        dest.writeLong(this.runOutDate != null ? this.runOutDate.getTime() : -1);
        dest.writeInt(this.lastingDays);
        dest.writeBoolean(this.running);
        dest.writeList(this.list_Ids);
    }

    public void readFromParcel(Parcel source) {
        this.item_Id = source.readInt();
        this.itemName = source.readString();
        this.price = source.readFloat();
        this.amount = source.readInt();
        long tmpRunOutDate = source.readLong();
        this.runOutDate = tmpRunOutDate == -1 ? null : new Date(tmpRunOutDate);
        this.lastingDays = source.readInt();
        this.running = source.readBoolean();
        source.readList(this.list_Ids, Integer.class.getClassLoader());
    }

    protected Item(Parcel in) {
        this.item_Id = in.readInt();
        this.itemName = in.readString();
        this.price = in.readFloat();
        this.amount = in.readInt();
        long tmpRunOutDate = in.readLong();
        this.runOutDate = tmpRunOutDate == -1 ? null : new Date(tmpRunOutDate);
        this.lastingDays = in.readInt();
        this.running = in.readBoolean();
        in.readList(this.list_Ids, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel source) {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}
