package com.ignas.android.groceryshoppingapp.Logic;


import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.RealmDb;

import java.util.ArrayList;
import java.util.List;

public class PageViewModel {//extends ViewModel {
    private static PageViewModel pageViewModel=null ;

    private ArrayList<Item> items =null;
    RealmDb db;

    public PageViewModel() {
        db = new RealmDb();
    }
    public static PageViewModel getInstance(){
        if(pageViewModel==null){
           pageViewModel = new PageViewModel();
        }
        return pageViewModel;
    }

    public void setItems(){
       items =  db.getItems();
    }

    public ArrayList<Item> getItems() {
        return items;
    }
    /*
   private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, input -> "Hello world from section: " + input);
    public void setIndex(int index) {mIndex.setValue(index);}
    public LiveData<String> getText() {return mText;}

 */






}