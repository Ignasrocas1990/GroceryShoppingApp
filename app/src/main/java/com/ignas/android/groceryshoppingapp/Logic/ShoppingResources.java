package com.ignas.android.groceryshoppingapp.Logic;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Models.ShoppingItem;

import java.util.ArrayList;
import java.util.Random;

public class ShoppingResources {

    private final MutableLiveData<ArrayList<ShoppingItem>> liveSpItems = new MutableLiveData<>();
    private final MutableLiveData<Float> liveTotal = new MutableLiveData<>();


//creates shopping day items to be displayed
    public void createItems(ArrayList<Item> items,ArrayList<Association> displayAssos,ArrayList<ItemList> lists ){
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
        liveSpItems.setValue(spItems);
    }

    public void addSPItem(String name,int amount,float price){

        Random r = new Random();
        ArrayList<ShoppingItem> tempItems = liveSpItems.getValue();
        if(tempItems ==null){
            tempItems = new ArrayList<>();
        }
        ShoppingItem newItem = new ShoppingItem(r.nextInt(),name, amount, price);

        tempItems.add(newItem);
        liveSpItems.setValue(tempItems);
    }



//total methods (simple add/subtract/set)
    public void setTotal(float price) {
        liveTotal.setValue(price);
    }
    public void addToTotal(float price){
        try{
            liveTotal.setValue(liveTotal.getValue()+price);
        }catch(NullPointerException e){
            Log.i("log", "addToTotal: "+e.getMessage());
        }
    }
    public void subtractTotal(float price){
        try{
            liveTotal.setValue(liveTotal.getValue()-price);
        }catch(NullPointerException e){
            Log.i("log", "addToTotal: "+e.getMessage());
        }
    }
    public ArrayList<ShoppingItem> getShoppingItems(){
        return liveSpItems.getValue();
    }

}
