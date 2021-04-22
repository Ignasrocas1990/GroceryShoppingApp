package com.ignas.android.groceryshoppingapp.View.Layer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ignas.android.groceryshoppingapp.Models.ItemList;

public class CurListViewModel extends ViewModel {

    MutableLiveData<ItemList> currentList = new MutableLiveData<>();
    private ItemList list_to_del;

    public CurListViewModel(@NonNull Application application) {
        super(application);
    }
    public ItemList getDeleteList(){
        return list_to_del;
    }
    public void deleted(){ list_to_del=null;}

    public ItemList setItemtoDel(){
        list_to_del = currentList.getValue();
        return list_to_del;
    }

    public void setCurrentList(ItemList current){
        currentList.setValue(current);
    }

    public LiveData<ItemList> getCurrentList() {
        return currentList;
    }


}
