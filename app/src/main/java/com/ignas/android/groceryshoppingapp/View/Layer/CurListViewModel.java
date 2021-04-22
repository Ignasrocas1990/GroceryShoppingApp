package com.ignas.android.groceryshoppingapp.View.Layer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.ignas.android.groceryshoppingapp.Models.ItemList;

public class CurListViewModel extends ViewModel {

    MutableLiveData<ItemList> currentList = new MutableLiveData<>();

    public CurListViewModel(@NonNull Application application) {
        super(application);
    }


    public void setCurrentList(ItemList current){
        currentList.setValue(current);
    }

    public LiveData<ItemList> getCurrentList() {
        return currentList;
    }


}
