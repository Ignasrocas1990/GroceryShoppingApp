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

public class ShoppingDay {

    private final MutableLiveData<ArrayList<ShoppingItem>> liveSpList = new MutableLiveData<>();
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
        liveSpList.setValue(spItems);
    }

    public void addSPItem(String name,int amount,float price){

        Random r = new Random();
        ArrayList<ShoppingItem> tempItems = liveSpList.getValue();
        if(tempItems ==null){
            tempItems = new ArrayList<>();
        }
        ShoppingItem newItem = new ShoppingItem(r.nextInt(),name, amount, price);
        tempItems.add(newItem);
        liveSpList.setValue(tempItems);
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


//live methods
    public LiveData<Float> getLiveTotal() {
        return liveTotal;
    }
    public LiveData<ArrayList<ShoppingItem>> getLiveSpList() {
        return liveSpList;
    }




//for each item check if it runs out before two days.
    /*//TODO DELETE
    public ArrayList<Item> createShoppingItems(ArrayList<Item> items){
        //final long twoDays = 48*60*60*1000;
        long twoDays = 20*1000;
        final long now = Calendar.getInstance().getTimeInMillis();
        long runOutDate=0,leftOverTime =0;
        for(int i=0;i<items.size();i++){

            runOutDate = items.get(i).getRunOutDate().getTime();
            leftOverTime = runOutDate-now;

            if(twoDays <= leftOverTime){
                items.remove(items.get(i));
            }
        }
        return items;
    }

    //just removes items that not been notified(any item that is going to running out)
    public ArrayList<Item> createShoppingItems(ArrayList<Item> items){
        ArrayList<Item> copy = new ArrayList<Item>();
        for(int i=0;i<items.size();i++){

            Item current = items.get(i);

            if(current.isNotified()){
                copy.add(current);
            }
        }
        return copy;
    }
    */
    // TODO Delete(maybe)
/*
        public int getLeftOver(Item item){
        float itemPercent=0.f;
        final long now = Calendar.getInstance().getTimeInMillis();
        final long runOutDate = item.getRunOutDate().getTime();
        //final long itemTimeLength = item.getLastingDays()*24*60*60*1000;//len
        final long itemTimeLength = item.getLastingDays()*1000;//TODO -- swap for whats above

        final long onePercent = itemTimeLength/100;//1%
        final long startDate = runOutDate-itemTimeLength;
        final long currentValueTime = now-startDate; // current value
        itemPercent =(float) currentValueTime/onePercent;

        return Math.round(itemPercent/1000);
    }
*/
}
