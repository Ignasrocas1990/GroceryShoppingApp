package com.ignas.android.groceryshoppingapp.Logic;


import android.util.Log;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Service.RealmDb;

import java.util.ArrayList;

public class PageViewModel {
    private static final String TAG ="log" ;//extends ViewModel {
    private static PageViewModel pageViewModel=null ;

    private ArrayList<Item> app_items;

    RealmDb db;

    public PageViewModel() {
        db = new RealmDb();
        //db.removeAll();
        app_items=setItems();
        addEmpty();
    }
    public static PageViewModel getInstance(){
        if(pageViewModel==null){
           pageViewModel = new PageViewModel();
        }
        return pageViewModel;
    }

    private ArrayList<Item> setItems(){
       return db.getItems();
    }

    public ArrayList<Item> getItems() {
        return app_items;
    }
    public void addEmpty(){
        if(app_items.size()==0 || !app_items.get(app_items.size()-1).getItemName().equals("")) {
            app_items.add(new Item());
        }

    }

    /*
   private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, input -> "Hello world from section: " + input);
    public void setIndex(int index) {mIndex.setValue(index);}
    public LiveData<String> getText() {return mText;}

 */

}