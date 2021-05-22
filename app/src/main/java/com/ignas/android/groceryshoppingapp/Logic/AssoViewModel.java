package com.ignas.android.groceryshoppingapp.Logic;

import android.annotation.SuppressLint;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Service.Repository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class AssoViewModel extends ViewModel {
    private final int NONE = 0;
    private final Repository repository;
    private final MutableLiveData<List<Association>> currentLive = new MutableLiveData<>();
    private List<Association> boughtAssos = new ArrayList<>();
    private final MutableLiveData<ArrayList<String>> dateDisplay = new MutableLiveData<>();
    private final MutableLiveData<List<ItemList>> boughtLists = new MutableLiveData<>();




    //constructor
    public AssoViewModel(){
        repository = Repository.getInstance();
    }

    public LiveData<List<Association>> getCurrentLive() {
        return currentLive;
    }
    public List<Association> getCurrentAsso(){return currentLive.getValue();}


//find association and set it to current
    public void setAsso(int list_Id){
        List<Association> associations = repository.getAsso(list_Id);
        currentLive.setValue(associations);
    }
//add association
    public void addAsso(int list_Id,int item_Id,int quantity){
        Association newAsso = new Association(list_Id, item_Id, quantity);
        List<Association> current = currentLive.getValue();
        repository.saveAsso(newAsso);
        current.add(newAsso);

        currentLive.setValue(current);
    }
    //set multiple items to be deleted from one list
    public void severList(List<Association> listToDelete){
        if(listToDelete.size() !=0){

            for(int i=0;i<listToDelete.size();i++){

                deleteFromCurrent(listToDelete.get(i).getItem_Id(),listToDelete);
                i--;
            }
        }
    }

//del association
    public void deleteAsso(int item_Id){
        currentLive.setValue(deleteFromCurrent(item_Id,currentLive.getValue()));
    }
//delete single association method from current live list of associations
    public List<Association> deleteFromCurrent(int item_Id, List<Association> curr) {
        if(curr==null){
            return null;
        }
        Association asso = curr.stream()
                .filter(a -> a.getItem_Id() == item_Id)
                .findFirst().orElse(null);

        if (asso != null) {
            asso.setDeleteFlag(true);
            repository.saveAsso(asso);
            curr.remove(asso);
        }
        return curr;

    }

    public List<Association> getBoughtAssos() {
        return boughtAssos;
    }


    public void removeListAssos(ItemList list) {
        List<Association> deleted = repository.getAsso(list.getList_Id());
        severList(deleted);
    }

    //finds instances of bought items that selected in the report drop down menu.
    public void itemQuery(int item_Id){
        if(item_Id!=-1){
            boughtAssos = repository.findBoughtInstances(item_Id);
            boughtLists.setValue(repository.findListsQuery(boughtAssos));
        }else{
            boughtAssos.clear();
        }
    }

//compile shopping associations
    public HashMap<Integer, ArrayList<Association>> findShoppingAssos(ArrayList<ItemList> lists, ArrayList<Item> notifiedItems) {
        HashMap<Integer,ArrayList<Association>> listMap = new HashMap<>();
        if(notifiedItems.size()==0){
            return listMap;
        }else if(lists.size()!= 0){
            ArrayList <Association> notifiedAssos =  findNotifiedAssos(notifiedItems);

            for(Association currAsso: notifiedAssos) {
                for(ItemList currList:lists){

                    if(currAsso.getList_Id()==0){//check if item is wild(part of all lists)

                        if(listMap.containsKey(currList.getList_Id())){//check if listmap already has that list into it
                            listMap.get(currList.getList_Id()).add(currAsso);
                        }else{
                            ArrayList<Association> temp = new ArrayList<>();
                            temp.add(currAsso);
                            listMap.put(currList.getList_Id(),temp);
                        }
                    }else if(currList.getList_Id() == currAsso.getList_Id()){

                        if(listMap.containsKey(currList.getList_Id())){//check if listmap already has that list into it
                            listMap.get(currList.getList_Id()).add(currAsso);
                        }else{
                            ArrayList<Association> temp = new ArrayList<>();
                            temp.add(currAsso);
                            listMap.put(currList.getList_Id(),temp);
                        }
                    }
                }
            }

        }else{ // creates a list of items if there are not lists but items have run out
            ItemList allLists = new ItemList("all","every");
            allLists.setList_Id(0);
            ArrayList<Association> aForAllLists = new ArrayList<>();
            for(Item curr: notifiedItems){
                aForAllLists.add(new Association(curr.getItem_id()));
            }
            listMap.put(allLists.getList_Id(),aForAllLists);
            lists.add(allLists);
        }
        return listMap;
    }
    public ArrayList<Association> findNotifiedAssos(ArrayList<Item> notifiedItems){
        ArrayList<Association> allAssos = new ArrayList<>(repository.getAllAssos());
        ArrayList<Association> foundAssos = new ArrayList<>();
        boolean found;

        for(Item item : notifiedItems){
            found = false;
            for(Association asso : allAssos){
                if(item.getItem_id()==asso.getItem_Id()){
                    foundAssos.add(asso);
                    found = true;
                }
            }
            if(!found){
                foundAssos.add(new Association(item.getItem_id()));
            }
        }
        return foundAssos;
    }

//get associations that item connected to
    public List<Association> apartOfList(Item item) {
        List<Association> assos = repository.findItemAssos(item.getItem_id());
        return assos;
    }

//removes all connections that item is about to be deleted
    public void removeItemsAssos(List<ItemList> removed, Item deleteItem){
        repository.severItem(removed,deleteItem);
    }

//checks and eather remove asso that has no list or with a list
    public void onBuyBought(Association currentAsso, HashMap<Integer, ArrayList<Association>> shoppingAssos) {
        if(currentAsso.getList_Id()==0){
            removeWildAsso(currentAsso,shoppingAssos);
        }else{
            removeBoughtAsso(currentAsso,shoppingAssos);
        }
    }
    public void insertOnFlyItemAsso(Association asso, HashMap<Integer, ArrayList<Association>> shoppingAssos){
        for(ArrayList<Association> curAssoArray:shoppingAssos.values()){
            curAssoArray.add(asso);
        }
    }
    public void markAsBought(int item_id, String amountString){
        Association bought = createTempAsso(item_id,amountString);
        bought.setDeleteFlag(true);
        bought.setBought(true);

        repository.saveAsso(bought);
    }
//create temp asso(not saved)
    public Association createTempAsso(int item_id, String amountString) {
        int amount=0;
        if(!amountString.equals("")){
            amount= Integer.parseInt(amountString);
        }
        return new Association(item_id,amount);


    }

 //get database associations for grouping
    public HashMap<String, List<Association>> findAssosByDate() {
        ArrayList<String>foundDates = new ArrayList<>();
        List<Association> boughtAssos = repository.getBoughtAssos();

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
        //date.add(Calendar.DAY_OF_WEEK,-7);//un comment this to go back to noraml days (As Cris suggested) TODO
        date.add(Calendar.MINUTE,-30);
        return date.getTimeInMillis();
    }
    public long getEnd(long key){
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(key);
        //date.add(Calendar.DAY_OF_WEEK,7);//un comment this to go back to noraml days (As Cris suggested) TODO
        date.add(Calendar.MINUTE,30);
        return date.getTimeInMillis();

    }

//filter associations by selected item
    public List<Association> filterAssos(List<Association> currAsso, Item item) {

        List<Association> newAssos = new ArrayList<>();
        for(int i=0;i<currAsso.size();i++){
            if(currAsso.get(i).getItem_Id() == item.getItem_id()){
                newAssos.add(currAsso.get(i));
            }
        }
        return newAssos;
    }
//get association that common to both lists
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
    //remove association that are part of every list (get first to save)
    public void removeWildAsso(Association currentAsso, HashMap<Integer, ArrayList<Association>> shoppingAssos) {
        Set<Map.Entry<Integer, ArrayList<Association>>> keys = shoppingAssos.entrySet();
        Iterator<Map.Entry<Integer,ArrayList<Association>>> iterator = keys.iterator();
        boolean first = true;
        while(iterator.hasNext()){
            Map.Entry<Integer, ArrayList<Association>> current = iterator.next();
            ArrayList<Association> currAssos = current.getValue();
            for(int i =0;i< currAssos.size();i++){
                if(currAssos.get(i).getItem_Id() == currentAsso.getItem_Id()){
                    if(first) {
                        Association asso = currAssos.get(i);
                        asso.setBought(true);
                        asso.setDeleteFlag(true);
                        repository.saveAsso(asso);
                    }
                    currAssos.remove(i);
                    i--;
                    first=false;

                }
            }
        }
    }

    //remove association that are been bought
    public void removeBoughtAsso(Association currentAsso, HashMap<Integer, ArrayList<Association>> shoppingAssos){

        Set<Map.Entry<Integer, ArrayList<Association>>> keys = shoppingAssos.entrySet();
        for (Map.Entry<Integer, ArrayList<Association>> current : keys) {
            ArrayList<Association> currAssos = current.getValue();
            for (int i = 0; i < currAssos.size(); i++) {
                if (currAssos.get(i).getItem_Id() == currentAsso.getItem_Id() &&
                        currAssos.get(i).getList_Id() == currentAsso.getList_Id()) {

                    Association asso = currAssos.get(i);
                    Association boughtAsso = new Association(asso.getList_Id(),asso.getItem_Id(),asso.getQuantity());
                    boughtAsso.setBought(true);
                    boughtAsso.setDeleteFlag(true);
                    repository.saveAsso(boughtAsso);

                    currAssos.remove(i);
                    i--;
                } else if (currAssos.get(i).getItem_Id() == currentAsso.getItem_Id()) {
                    currAssos.remove(i);
                    i--;
                }
            }
        }
    }



    public LiveData<List<ItemList>> getBoughtLists() {
        return boughtLists;
    }

    public LiveData<ArrayList<String>> getDateDisplay() {
        return dateDisplay;
    }

}