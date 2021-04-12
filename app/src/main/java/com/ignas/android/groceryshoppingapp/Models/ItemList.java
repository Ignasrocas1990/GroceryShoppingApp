package com.ignas.android.groceryshoppingapp.Models;

import java.util.ArrayList;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ItemList{

    @PrimaryKey
    private int listID=0;

    private String listName="";
    private float totalPrice=0;
    private String shopName="";
    private ArrayList<Item> items = new ArrayList<Item>();
    private Item item=null;

    {
        this.listID++;
    }
    public ItemList(){}
    public ItemList(String listName, String shopName) {
        this.listName = listName;
        this.shopName = shopName;
    }
    public ItemList(String listName) {
        this.listName = listName;
    }

    //items manipulation methods
    public Item createItem(String itemName,float price,int amount,int lastingDays){
        item = new Item(itemName,price,amount,lastingDays);
        return item;
    }
    public void addItem(Item item){
        if(item != null){
            items.add(item);
            float itemPrice = item.getPrice();
            if(itemPrice != 0){
                totalPrice+=itemPrice;
            }
        }
    }
    public void removeItem(Item item){
        if(!items.isEmpty()){
            items.remove(item);
            float itemPrice = item.getPrice();
            if(itemPrice != 0){
                totalPrice-=itemPrice;
            }
        }
    }

    //Setters & getters for ItemList
    public ArrayList<Item> getItems() {
        return items;
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
