package com.ignas.android.groceryshoppingapp.Models;

import org.bson.types.ObjectId;

import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Association extends RealmObject {

    @PrimaryKey
    private int asso_Id;
    {
        Random r = new Random();
        asso_Id = r.nextInt();
    }

    private int list_Id;
    private int item_Id;
    private int quantity=0;

    //constructor
    public Association(){}
    public Association(int list_Id, int item_Id, int quantity) {
        this.list_Id = list_Id;
        this.item_Id = item_Id;
        this.quantity = quantity;
    }

    //getters & setters
    public int getAsso_Id() {
        return asso_Id;
    }

    public int getList_Id() {
        return list_Id;
    }

    public void setList_Id(int list_Id) {
        this.list_Id = list_Id;
    }

    public int getItem_Id() {
        return item_Id;
    }

    public void setItem_Id(int item_Id) {
        this.item_Id = item_Id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
