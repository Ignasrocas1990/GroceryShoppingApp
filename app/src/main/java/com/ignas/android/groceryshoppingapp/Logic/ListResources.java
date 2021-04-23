package com.ignas.android.groceryshoppingapp.Logic;

import android.content.Context;

import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.util.ArrayList;

public class ListResources {

    Context mContext = null;
    RealmDb db;

    public ListResources(Context context) {
        db = new RealmDb();
        mContext = context;
    }
    public ArrayList<ItemList> getLists() {
        return db.getLists();
    }

    public void setContext(Context context){
        mContext = context;
    }

    public void updateLists(ArrayList<ItemList> app_lists) {
        int i;
        ArrayList<ItemList>dbLists = db.getLists();
        ArrayList<ItemList>newLists = new ArrayList<>();
        if(dbLists.size()!=0) {
            for (i = 0; i < app_lists.size(); i++) {

                while (i < dbLists.size() && dbLists.get(i).getList_Id() !=  app_lists.get(i).getList_Id()) {
                    db.removeList(dbLists.get(i));
                    dbLists.remove(dbLists.get(i));
                }
                if(i >= dbLists.size() || !dbLists.get(i).equals(app_lists.get(i))){
                    newLists.add(app_lists.get(i));
                }
            }
            if(dbLists.size()>app_lists.size()) {
                for (i = app_lists.size(); i < dbLists.size(); i++) {
                    db.removeList(dbLists.get(i));
                }
            }
        }else{
            newLists.addAll(app_lists);
        }
        if(newLists.size()>1){
            db.addLists(newLists);
        }else if(newLists.size()==1){
            db.addList(newLists.get(0));
        }
    }


}
