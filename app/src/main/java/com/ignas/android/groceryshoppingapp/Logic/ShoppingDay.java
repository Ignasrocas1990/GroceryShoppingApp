package com.ignas.android.groceryshoppingapp.Logic;

import com.ignas.android.groceryshoppingapp.Models.Item;

import java.util.ArrayList;
import java.util.Calendar;

public class ShoppingDay {
//for each item check if it runs out before two days.
    /*
    public ArrayList<Item> createShoppingItems(ArrayList<Item> items){
        //final long twoDays = 48*60*60*1000;
        long twoDays = 20*1000;//TODO---testing with seconds (need to change to above)
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
     */
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


}
