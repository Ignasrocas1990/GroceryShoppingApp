package com.ignas.android.groceryshoppingapp.Logic;


import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.RealmDb;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class PageViewModel {//extends ViewModel {
    private static PageViewModel pageViewModel=null ;

    private ArrayList<Item> items;
    RealmDb db;

    public PageViewModel() {
        db = new RealmDb();
        setItems();

       // db.removeAll();

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

    public void saveItems(){
        for(int i = 0;i< items.size();i++){
            if(items.get(i).getItemName().equals("")){
                    items.remove(i);
            }
        }
        db.addItems(items);
    }
    /*
   private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, input -> "Hello world from section: " + input);
    public void setIndex(int index) {mIndex.setValue(index);}
    public LiveData<String> getText() {return mText;}

 */
}