package com.ignas.android.groceryshoppingapp.View.Lists;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.AssoRepository;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class AssoViewModel extends ViewModel {
    private final int NONE = 0;
    private final AssoRepository assoRepository;
    private final MutableLiveData<List<Association>> currentLive = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<String>> dateDisplay = new MutableLiveData<>();

    //constructor
    public AssoViewModel(){
        assoRepository = new AssoRepository();
    }

    public LiveData<List<Association>> getCurrentLive() {
        return currentLive;
    }
    public List<Association> getCurrentAsso(){return currentLive.getValue();}


//find association and set it to current
    public void setAsso(int list_Id){
        List<Association> associations = assoRepository.getAsso(list_Id);
        currentLive.setValue(associations);
    }
//add association
    public void addAsso(int list_Id,int item_Id,int quantity){
        currentLive.setValue(assoRepository.addAsso(list_Id,item_Id,quantity,currentLive.getValue()));
    }

//del association
    public void deleteAsso(int item_Id){
        currentLive.setValue(assoRepository.deleteAsso(item_Id,currentLive.getValue()));
    }
    public void removeListAssos(ItemList list) {
        List<Association> deleted = assoRepository.getAsso(list.getList_Id());
        assoRepository.severList(deleted);
    }
//compile shopping associations
    public HashMap<Integer, ArrayList<Association>> findShoppingAssos(ArrayList<ItemList> lists, ArrayList<Item> notifiedItems) {
        return assoRepository.findShoppingAssos(lists,notifiedItems);
    }

    public List<Association> apartOfList(Item item) {
        List<Association> assos = assoRepository.findItemAssos(item.getItem_id());
        return assos;

    }
    public void removeItemsAssos(List<ItemList> removed, Item deleteItem){
        assoRepository.severItem(removed,deleteItem);
    }
/*
    public ArrayList<Association> findAssociations(ArrayList<Item> items){
        return assoResources.findAssociations(items);
    }

 */
    public void onBuyBought(Association currentAsso, HashMap<Integer, ArrayList<Association>> shoppingAssos) {
        if(currentAsso.getList_Id()==0){
            assoRepository.removeWildAsso(currentAsso,shoppingAssos);
        }else{
            assoRepository.removeBoughtAsso(currentAsso,shoppingAssos);
        }
    }
    public void insertOnFlyItemAsso(Association asso, HashMap<Integer, ArrayList<Association>> shoppingAssos){
        assoRepository.insertOnFlyItemAsso(asso,shoppingAssos);
    }
    public void markAsBought(int item_id, String amountString){
        Association bought = createTempAsso(item_id,amountString);
        bought.setDeleteFlag(true);
        bought.setBought(true);

        assoRepository.addAsso(bought);
    }

    public Association createTempAsso(int item_id, String amountString) {
        int amount=0;
        if(!amountString.equals("")){
            amount= Integer.parseInt(amountString);
        }
        return new Association(item_id,amount);


    }
    public HashMap<String, List<Association>> findAssosByDate() {
        ArrayList<String>foundDates = new ArrayList<>();
        List<Association> boughtAssos = assoRepository.getBoughtAssos();

        HashMap<Long, List<Association>> groups = groupByDate(boughtAssos);
        HashMap<String, List<Association>> displayGroups = convertToString(groups);

        if(groups.keySet().isEmpty()){
            foundDates.add(0,"no items bought");
            dateDisplay.setValue(foundDates);
        }else{
            foundDates = new ArrayList<>(displayGroups.keySet());
            foundDates.add(0,"select date");
        }
        dateDisplay.setValue(foundDates);
        return displayGroups;
    }
//get Associations for date spinner in report
    private HashMap<Long, List<Association>> groupByDate(List<Association> boughtAssos) {
        HashMap<Long, List<Association>> groupedAssos = new HashMap<>();
        Calendar currentTime = Calendar.getInstance();
        boolean found;
        long end,start;
        ArrayList<Association> currentAssos;

        for(Association asso : boughtAssos){

            Date cur =  asso.getBoughtDate();
            if(cur!=null){

            currentTime.setTime(cur);

            ArrayList<Long>keys = new ArrayList<>(groupedAssos.keySet());
            if(keys.size()!=0){
                found = false;
                for(Long key : keys) {
                    start = getStart(key);
                    end = getEnd(key);
                    if(currentTime.getTimeInMillis() >= start && currentTime.getTimeInMillis() <= end){

                        Objects.requireNonNull(groupedAssos.get(key)).add(asso);
                        found=true;
                        break;
                    }
                }
                if(!found){
                    currentAssos = new ArrayList<>();
                    currentAssos.add(asso);
                    groupedAssos.put(currentTime.getTimeInMillis(),currentAssos);
                }
            }else{
                currentAssos = new ArrayList<>();
                currentAssos.add(asso);
                groupedAssos.put(currentTime.getTimeInMillis(),currentAssos);
                }
            }
        }

        return groupedAssos;
    }
    public HashMap<String, List<Association>> convertToString(HashMap<Long, List<Association>> groupedAssos ){

        HashMap<String, List<Association>> newGroupedAssos = new HashMap<>();
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("d/M/yyyy k:m");
        Calendar keyDate = Calendar.getInstance();
        for(Long key : groupedAssos.keySet()){
            keyDate.setTimeInMillis(key);
            newGroupedAssos.put(formatter.format(keyDate.getTime()),groupedAssos.get(key));
        }
        return newGroupedAssos;
    }
    private long getStart(long key){
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(key);
        //date.add(Calendar.DAY_OF_WEEK,-7);//un comment this to go back to noraml days (As Cris suggested)
        date.add(Calendar.MINUTE,-30);
        return date.getTimeInMillis();
    }
    public long getEnd(long key){
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(key);
        //date.add(Calendar.DAY_OF_WEEK,7);//un comment this to go back to noraml days (As Cris suggested)
        date.add(Calendar.MINUTE,30);
        return date.getTimeInMillis();

    }

    public LiveData<ArrayList<String>> getDateDisplay() {
        return dateDisplay;
    }

    public List<Association> filterAssos(List<Association> currAsso, Item item) {

        List<Association> newAssos = new ArrayList<>();
        for(int i=0;i<currAsso.size();i++){
            if(currAsso.get(i).getItem_Id() == item.getItem_id()){
                newAssos.add(currAsso.get(i));
            }
        }
        return newAssos;
    }
    public List<Association> getCommon(List<Association>itemAssos,List<Association>dateAssos){
        List<Association>filteredAssos =  new ArrayList<>();
        for(Association iAsso : itemAssos){
            Association found = dateAssos.stream()
                    .filter(dAsso->dAsso.getAsso_Id() == iAsso.getAsso_Id()).findFirst().orElse(null);
            if(found !=null){
                filteredAssos.add(found);
            }
        }

         return filteredAssos;
    }
}