package com.ignas.android.groceryshoppingapp.Models;

import java.util.ArrayList;
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
    private RealmList<Integer> itemIds = new RealmList<Integer>();
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

    public void addItem(int itemId,float price){
        itemIds.add(itemId);
        totalPrice +=price;
    }
    public void removeItem(int itemId,float price){
        if(!itemIds.isEmpty()){
            itemIds.remove(itemId);
            if(price != 0){
                totalPrice-=price;
            }
        }
    }

    //Setters & getters
    public int getList_Id(){
        return list_Id;
    }
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public String getShopName() {
        return shopName;
    }
    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
