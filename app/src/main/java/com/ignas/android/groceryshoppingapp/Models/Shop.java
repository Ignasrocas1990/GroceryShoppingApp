package com.ignas.android.groceryshoppingapp.Models;

import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
/***
 * Author:Ignas Rocas
 * Student Id: C00135830
 * Date: 28/05/2021
 * Purpose: Project, shop model
 */
public class Shop extends RealmObject {

    @PrimaryKey
    private int shop_Id;
    private String shopName="all";
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
