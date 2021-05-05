package com.ignas.android.groceryshoppingapp.Logic;

import com.ignas.android.groceryshoppingapp.Models.Item;

import java.util.ArrayList;
import java.util.Calendar;

public class ShoppingDay {
//for each item check if it runs out before two days.
    public ArrayList<Item> createShoppingItems(ArrayList<Item> items){
        //final long twoDays = 48*60*60*1000;
        long twoDays = 2*1000;//TODO---testing with seconds (need to change to above)
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


}
