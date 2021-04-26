package com.ignas.android.groceryshoppingapp.Models;


import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemList extends RealmObject {

    @PrimaryKey
    private int list_Id;
    private String listName="";
    private Shop shop;

    //constructors
    {
        shop = new Shop();
        Random r = new Random();
        list_Id = r.nextInt();
    }
    public ItemList(){}
    public ItemList(String listName) {
        this.listName = listName;
    }
    public ItemList(String listName, String shopName) {
        this.listName = listName;
        shop.setShopName(shopName);
    }

    //Setters & getters
    public void setShopName(String shopName) {
        shop.setShopName(shopName);
    }
    public void setListName(String listName) {
        this.listName = listName;
    }
    public void setList_Id(int list_Id) {
        this.list_Id = list_Id;
    }

    public String getShopName() {
        return shop.getShopName();
    }
    public String getListName() {
        return listName;
    }
    public int getList_Id() {
        return list_Id;
    }

}
