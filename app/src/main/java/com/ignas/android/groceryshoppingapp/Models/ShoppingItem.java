
package com.ignas.android.groceryshoppingapp.Models;

public class ShoppingItem{
     int item_Id=0;
     int asso_Id=0;
     int list_Id=0;
     String name="";
     float price=0.f;
     int quantity=0;
     String shopName="";
     String listName="";
     boolean selected = false;

    public ShoppingItem(){}
    public ShoppingItem(int item_Id, String name, float price) {
        this.item_Id = item_Id;
        this.name = name;
        this.price = price;
    }
    public ShoppingItem(int item_Id, String name,int quantity, float price) {
        this.item_Id = item_Id;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public ShoppingItem(int item_Id, int asso_Id, int list_Id, String name, float price,int quantity, String shopName, String listName) {
        this.item_Id = item_Id;
        this.asso_Id = asso_Id;
        this.list_Id = list_Id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.shopName = shopName;
        this.listName = listName;
    }

    public int getItem_Id() {
        return item_Id;
    }

    public void setItem_Id(int item_Id) {
        this.item_Id = item_Id;
    }

    public int getAsso_Id() {
        return asso_Id;
    }

    public void setAsso_Id(int asso_Id) {
        this.asso_Id = asso_Id;
    }

    public int getList_Id() {
        return list_Id;
    }

    public void setList_Id(int list_Id) {
        this.list_Id = list_Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
