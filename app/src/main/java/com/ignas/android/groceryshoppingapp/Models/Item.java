package com.ignas.android.groceryshoppingapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Item extends RealmObject implements Parcelable {

    //ObjectId item_id = new ObjectId(Integer.toString(new Random().nextInt()));

    @PrimaryKey
    private int item_iD;
    {
        Random r = new Random();
        item_iD = r.nextInt();
        setRunOutDate(7);

    }

    private String itemName="";
    private float price=0;
    private int amount=0;
    private Date runOutDate;
    private int lastingDays=0;

    public Item(){}
    public Item(String itemName) {
        this.itemName = itemName; }

    public Item(String itemName, float price, int amount, int lastingDays) {
        this.itemName = itemName;
        this.price = price;
        this.amount = amount;
        this.lastingDays = lastingDays;
        setRunOutDate(lastingDays);
    }

    public int getItem_id() {
        return item_iD;
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
            calendar.add(Calendar.DAY_OF_WEEK,lastingDays);
            this.runOutDate = calendar.getTime();
        }else{
            calendar.add(Calendar.DAY_OF_WEEK,7);
            this.runOutDate = calendar.getTime();
        }


    }

    public int getLastingDays() {
        return lastingDays;
    }

    public void setLastingDays(int lastingDays) {
        this.lastingDays = lastingDays;
    }

    //Parcelable methods
    
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeFloat(this.price);
        dest.writeInt(this.amount);
        dest.writeLong(this.runOutDate != null ? this.runOutDate.getTime() : -1);
        dest.writeInt(this.lastingDays);
    }

    public void readFromParcel(Parcel source) {
        this.itemName = source.readString();
        this.price = source.readFloat();
        this.amount = source.readInt();
        long tmpRunOutDate = source.readLong();
        this.runOutDate = tmpRunOutDate == -1 ? null : new Date(tmpRunOutDate);
        this.lastingDays = source.readInt();
    }

    protected Item(Parcel in) {
        this.itemName = in.readString();
        this.price = in.readFloat();
        this.amount = in.readInt();
        long tmpRunOutDate = in.readLong();
        this.runOutDate = tmpRunOutDate == -1 ? null : new Date(tmpRunOutDate);
        this.lastingDays = in.readInt();
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
    //for comparing objects
    @Override
    public boolean equals(Object obj) {
        if(this == obj)return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        Item i = (Item) obj;
        return item_iD == i.getItem_id() && itemName.equals(i.getItemName())
                && Float.compare(price,i.getPrice())==0 && amount == i.getAmount()
                && (runOutDate.compareTo(i.getRunOutDate())==0) && lastingDays == i.getLastingDays();
    }
}
