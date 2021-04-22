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
    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
    public void setListName(String listName) {
        this.listName = listName;
    }
    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
    public void setItemIds(RealmList<Integer> itemIds) {
        this.itemIds = itemIds;
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
    public RealmList<Integer> getItemIds() {
        return itemIds;
    }
    public int getList_Id() {
        return list_Id;
    }


    public boolean equals(Object obj) {
        if(this == obj)return true;
        if(obj == null || getClass() != obj.getClass()) return false;
        ItemList i = (ItemList) obj;
        if (itemIds.size() != i.getItemIds().size()) return false;
        if(listName.equals(i.getListName()) && Float.compare(totalPrice,i.getTotalPrice())==0
                && shopName.equals(i.getShopName())) {
            if(itemIds.equals(i.getItemIds())) return true;
            Collections.sort(itemIds);
            Collections.sort(i.getItemIds());
            return itemIds.equals(i.getItemIds());
        }
        return false;
    }

}
