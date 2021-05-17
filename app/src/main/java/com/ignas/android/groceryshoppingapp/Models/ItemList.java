package com.ignas.android.groceryshoppingapp.Models;


import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemList extends RealmObject {

    @PrimaryKey
    private int list_Id;
    private String listName="";
    private Shop shop;
    private String toStringText = "";
    private boolean deleteFlag = false;

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
        defaultDisplayString();
    }

    //Setters & getters

    public boolean isDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(boolean deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

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

    private void defaultDisplayString(){
        this.toStringText = "List: "+getListName()+",Shop: "+shop.getShopName();
    }

    public String getToStringText() {
        return toStringText;
    }

    public void setToStringText(String toStringText) {
        this.toStringText = toStringText;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return toStringText;
    }
}
