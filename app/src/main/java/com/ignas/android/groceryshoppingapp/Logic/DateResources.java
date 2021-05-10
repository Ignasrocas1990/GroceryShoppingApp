package com.ignas.android.groceryshoppingapp.Logic;

import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Models.ShoppingItem;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;
import java.util.Random;

public class DateResources {
    private final RealmDb db;
    private final boolean dbSwitch;


    public DateResources() {
        db = new RealmDb();
        dbSwitch = db.getSwitch();
    }

//notification switch methods
    public boolean getDBSwitch(){
        return dbSwitch;
    }

    public Boolean updateSwitch(boolean appSwitch){

        if(appSwitch != dbSwitch){
            db.setSwitch(appSwitch);
        }
        if(appSwitch && !dbSwitch){
            return true;
        }
        return false;
    }
    //creates shopping day items to be displayed
    public ArrayList<ShoppingItem> createItems(ArrayList<Item> items, ArrayList<Association> displayAssos, ArrayList<ItemList> lists ){
        ArrayList<ShoppingItem> spItems = new ArrayList<>();
        ShoppingItem newItem;
        for(Association asso : displayAssos){

            Item currItem = items.stream().filter(item->item.getItem_id() == asso.getItem_Id())
                    .findFirst().orElse(null);

            ItemList currList = lists.stream().filter(list->list.getList_Id()==asso.getList_Id())
                    .findFirst().orElse(null);
            if(currItem!=null && currList!=null){

                newItem = new ShoppingItem(currItem.getItem_id(),asso.getAsso_Id(),currList.getList_Id()
                        ,currItem.getItemName(),currItem.getPrice(),asso.getQuantity(),currList.getShopName(),currList.getListName());
                spItems.add(newItem);
            }
        }
        if(spItems.size() == 0){
            for(Item i : items){
                newItem = new ShoppingItem(i.getItem_id(),i.getItemName(),i.getPrice());
                spItems.add(newItem);
            }
        }else{
            for(Item i : items){
                ShoppingItem found = spItems.stream()
                        .filter(spItem->spItem.getItem_Id()==i.getItem_id())
                        .findFirst().orElse(null);
                if(found==null){
                    newItem = new ShoppingItem(i.getItem_id(),i.getItemName(),i.getPrice());
                    spItems.add(newItem);
                }
            }
        }
        return spItems;
    }

    public ShoppingItem addSPItem(String name,int amount,float price){

        Random r = new Random();
        return new ShoppingItem(r.nextInt(),name, amount, price);
    }
}
