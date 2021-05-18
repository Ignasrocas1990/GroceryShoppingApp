package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ignas.android.groceryshoppingapp.Logic.DateResources;
import com.ignas.android.groceryshoppingapp.Models.Report;

import java.util.ArrayList;

public class DateViewModel extends ViewModel {

    private final MutableLiveData<Boolean> app_switch = new MutableLiveData<>();
    //shopping lives
    private final MutableLiveData<Float> liveTotal = new MutableLiveData<>();
    //report lives
    private final MutableLiveData<Report> liveReport = new MutableLiveData<>();


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


    //create first empty value
    public void setDefaultReportView(){
        Report emptyReport = new Report();
        emptyReport.setEmptyDateString("Please Select the date");
        reports.add(0,emptyReport);
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

//live observational methods
    public LiveData<Boolean> getLiveSwitch(){return app_switch;}
    public LiveData<Float> getLiveTotal() {
        return liveTotal;
    }
    public LiveData<Report> getLiveReport(){return liveReport;}


    public void findReportItems() {
        dateResources.findReportItems(liveReport.getValue());
    }
}
