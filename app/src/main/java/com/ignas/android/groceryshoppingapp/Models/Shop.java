package com.ignas.android.groceryshoppingapp.Models;

import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Shop extends RealmObject {

    @PrimaryKey
    private int shop_Id;
    private String shopName;
    {
        Random random = new Random();
        shop_Id = random.nextInt();
    }
    public Shop(){}
    public Shop(String shopName){
        this.shopName = shopName;
    }

    public int getShop_Id() {
        return shop_Id;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }
}
