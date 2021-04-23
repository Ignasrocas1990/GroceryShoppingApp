package com.ignas.android.groceryshoppingapp.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemList extends RealmObject {

    @PrimaryKey
    private int list_Id;

    private String listName="";
    private float totalPrice=0;
    private String shopName="";
    //constructors
    {
        Random r = new Random();
        list_Id = r.nextInt();
    }
    public ItemList(){}
    public ItemList(String listName) {
        this.listName = listName;
    }
    public ItemList(String listName, String shopName) {
        this.listName = listName;
        this.shopName = shopName;
    }

    //Setters & getters
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public void setListName(String listName) {
        this.listName = listName;
    }
    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setList_Id(int list_Id) {
        this.list_Id = list_Id;
    }

    public String getShopName() {
        return shopName;
    }
    public String getListName() {
        return listName;
    }
    public float getTotalPrice() {
        return totalPrice;
    }
    public int getList_Id() {
        return list_Id;
    }

    public boolean equals(Object obj) {
        if(this == obj)return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        ItemList i = (ItemList) obj;
        return (listName.equals(i.getListName()) && Float.compare(totalPrice,i.getTotalPrice())==0
                && shopName.equals(i.getShopName()));

    }
}
