package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.DateResources;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Models.Report;
import com.ignas.android.groceryshoppingapp.Models.ShoppingItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DateViewModel extends ViewModel {

    private final MutableLiveData<Boolean> app_switch = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<ShoppingItem>> liveSpItems = new MutableLiveData<>();
    private final MutableLiveData<Float> liveTotal = new MutableLiveData<>();
    private final MutableLiveData<Report> liveReport = new MutableLiveData<>();
    HashMap<Integer, ArrayList<ShoppingItem>> queryReports = new HashMap<>();
    private ArrayList<Report> reports;
    private final DateResources dateResources;


    public DateViewModel(){
        dateResources = new DateResources();
        app_switch.setValue(dateResources.getDBSwitch());
        this.reports  = dateResources.getReports();
        setDefaultReportView();
    }
//getters & setters
    public ArrayList<Report> getReports(){
        return reports;
    }
    public ArrayList<ShoppingItem> getShoppingItems(){
        return liveSpItems.getValue();
    }
    public boolean getSwitch() {
        return app_switch.getValue();
    }
    public Report getReport() {return liveReport.getValue();}

    public void setReport(Report report){
        liveReport.setValue(report);
    }
    public void setTotal(float price) {
        liveTotal.setValue(price);
    }
    public void setSwitch(boolean state) {
        app_switch.setValue(state);
    }
    public boolean updateSwitch(){
        return dateResources.updateSwitch(app_switch.getValue());
    }

    public void setQueryReports(HashMap<Integer, ArrayList<ShoppingItem>> queryReports) {
        this.queryReports = queryReports;
    }

    //create first empty value
    public void setDefaultReportView(){
        Report emptyReport = new Report();
        emptyReport.setEmptyDateString("Please Select the date");
        reports.add(0,emptyReport);
    }
//find displayItems by report id ---------------------
    public ArrayList<ShoppingItem> findShoppingContent(int reportId){
        if(queryReports.containsKey(reportId)){
            return queryReports.get(reportId);
        }
        return null;
    }
//creates shopping day items to be displayed
    public ArrayList<Item> createItems(ArrayList<Item> items, ArrayList<Association> displayAssos, ArrayList<ItemList> lists ){
        ArrayList<ShoppingItem> spItems = new ArrayList<>();
        ArrayList<Item> itemWithoutList = new ArrayList<>();
        ShoppingItem newItem;
        if(items==null){
            return null;
        }
        for(Association asso : displayAssos){

            Item currItem = items.stream().filter(item->item.getItem_id() == asso.getItem_Id())
                    .findFirst().orElse(null);

            ItemList currList = lists.stream().filter(list->list.getList_Id()==asso.getList_Id())
                    .findFirst().orElse(null);
            if(currItem!=null && currList!=null){

                newItem = new ShoppingItem(currItem.getItem_id(),asso.getAsso_Id(),currList.getList_Id()
                        ,currItem.getItemName(),currItem.getPrice(),asso.getQuantity(),currList.getShopName(),currList.getListName());
                spItems.add(newItem);
            }
        }
        if(spItems.size() == 0){
            for(Item i : items){
                newItem = new ShoppingItem(i.getItem_id(),i.getItemName(),i.getPrice());
                spItems.add(newItem);
                itemWithoutList.add(i);
            }
        }else{
            for(Item i : items){
                ShoppingItem found = spItems.stream()
                        .filter(spItem->spItem.getItem_Id()==i.getItem_id())
                        .findFirst().orElse(null);
                if(found==null){
                    newItem = new ShoppingItem(i.getItem_id(),i.getItemName(),i.getPrice());
                    spItems.add(newItem);
                    itemWithoutList.add(i);
                }
            }
        }
        liveSpItems.setValue(spItems);
        return itemWithoutList;
    }
    public int addSPItem(String name,int amount,float price){
        ShoppingItem addSPItem = dateResources.addSPItem(name, amount, price);
        ArrayList<ShoppingItem> liveSpItems = this.liveSpItems.getValue();
        if(liveSpItems==null){
            liveSpItems = new ArrayList<>();
        }
        liveSpItems.add(addSPItem);
        this.liveSpItems.setValue(liveSpItems);

        return addSPItem.getItem_Id();
    }
//total methods for shopping day (simple add/subtract/set)
    public void addToTotal(float price){
        try{
            liveTotal.setValue(liveTotal.getValue()+price);
        }catch(NullPointerException e){
            Log.i("log", "addToTotal: "+e.getMessage());
        }
    }
    public void subtractTotal(float price){
        try{
            liveTotal.setValue(liveTotal.getValue()-price);
        }catch(NullPointerException e){
            Log.i("log", "addToTotal: "+e.getMessage());
        }
    }
    public void createReport(float total,ArrayList<ShoppingItem> items){
        dateResources.createReport(total,items);
    }

//live observational methods
    public LiveData<Boolean> getLiveSwitch(){return app_switch;}
    public LiveData<Float> getLiveTotal() {
        return liveTotal;
    }
    public LiveData<ArrayList<ShoppingItem>> getLiveSpItems() {
        return liveSpItems;
    }
    public LiveData<Report> getLiveReport(){return liveReport;}


    public void findReportItems() {
        dateResources.findReportItems(liveReport.getValue());
    }
}
