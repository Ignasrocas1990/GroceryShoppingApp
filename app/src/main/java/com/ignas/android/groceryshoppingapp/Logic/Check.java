package com.ignas.android.groceryshoppingapp.Logic;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.util.ArrayList;

public class Check {
    public static boolean listEquals(ArrayList<ItemList> checkList, ItemList curr) {
            for (ItemList index : checkList) {
                if (index.getList_Id() == curr.getList_Id()) return true;
            }

        return false;
    }
    public static boolean itemEquals(ArrayList<Item> items, Item curr) {
        for (Item index : items) {
            if (index.getItem_id() == curr.getItem_id()) return true;
        }
        return false;
    }
    public static boolean assoEquals(ArrayList<Association> assoc, Association curr) {
        for (Association index : assoc) {
            if (index.getAsso_Id() == curr.getAsso_Id()) return true;
        }
        return false;
    }
}
