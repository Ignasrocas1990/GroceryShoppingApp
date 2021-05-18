package com.ignas.android.groceryshoppingapp.View.Report;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.View.Item.ItemViewModel;
import com.ignas.android.groceryshoppingapp.View.Lists.AssoViewModel;
import com.ignas.android.groceryshoppingapp.View.Lists.ListsViewModel;
import com.ignas.android.groceryshoppingapp.View.ShoppingDate.DateViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReportFragment extends Fragment {
    private final String TAG  = "log";
    Context context=null;
    ItemViewModel itemViewModel;
    ListsViewModel listViewModel;
    AssoViewModel assoViewModel;
    DateViewModel dateViewModel;
    RecyclerView recyclerView;
    Spinner itemSpinner,dateSpinner;


    public ReportFragment() { }

    public static ReportFragment newInstance() {
        return new ReportFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.report_recycler, container, false);
        context = view.getContext();
        listViewModel = ViewModelProviders.of(requireActivity()).get(ListsViewModel.class);
        assoViewModel = ViewModelProviders.of(requireActivity()).get(AssoViewModel.class);
        itemViewModel = ViewModelProviders.of(requireActivity()).get(ItemViewModel.class);
        dateViewModel = ViewModelProviders.of(requireActivity()).get(DateViewModel.class);

        itemSpinner = view.findViewById(R.id.itemSpinner);
        dateSpinner = view.findViewById(R.id.dateSpinner);
        recyclerView = view.findViewById(R.id.reportRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        final ArrayList<String>[] displayDates = new ArrayList[]{new ArrayList<String>()};
        ArrayList<Item> items =  itemViewModel.getBoughtItems();
        items = itemViewModel.setSpinnerText(items);

        //set up Recycler View
        ReportRecyclerViewAdapter reportAdapter = new ReportRecyclerViewAdapter(items);
        recyclerView.setAdapter(reportAdapter);

//set up date SPINNER(Drop down menu)
        HashMap<String, List<Association>> dateGroupAssos = assoViewModel.findAssosByDate();


        assoViewModel.getDateDisplay().observe(requireActivity(), strings -> {
            displayDates[0] = strings;

        });
        ArrayAdapter<String> dateArrayAdapter = new ArrayAdapter<>(context
                , android.R.layout.simple_spinner_item, displayDates[0]);
        dateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateArrayAdapter.setNotifyOnChange(true);
        dateSpinner.setAdapter(dateArrayAdapter);

//on select date in Date SPINNER
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    String date  = (String) parent.getItemAtPosition(position);
                    if(dateGroupAssos.containsKey(date)){
                        List<Association> assos = dateGroupAssos.get(date);

                        //find lists for each association selected

                        reportAdapter.fromDateSpinner(listViewModel.findLists(assos),assos);
                        reportAdapter.notifyDataSetChanged();
                    }
                }else{
                    reportAdapter.setDateSpinner(false);
                    reportAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//set item SPINNER (Drop Down menu)
        ArrayAdapter<Item> itemArrayAdapter = new ArrayAdapter<>(context
                , android.R.layout.simple_spinner_item, items);
        itemArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemArrayAdapter.setNotifyOnChange(true);
        itemSpinner.setAdapter(itemArrayAdapter);



        final Item[] selectedItem = {new Item()};
//at selected item in the item SPINNER
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    selectedItem[0] = (Item) parent.getItemAtPosition(position);
                    itemViewModel.itemQuery(selectedItem[0].getItem_id());
                }else{
                    reportAdapter.setItemSpinner(false);
                    reportAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//observer lists queried from database to recycler view
        itemViewModel.getBoughtLists().observe(requireActivity(), itemLists->{
            reportAdapter.fromItemSpinner(selectedItem[0],itemLists,itemViewModel.getBoughtAssos());
            reportAdapter.notifyDataSetChanged();

        });







        /*
        if(currentItems.size() != 0){
            items[0].clear();
            items[0].addAll(currentItems);
            Item displayItem = new Item("select item",0);
            items[0].add(0,displayItem);

        }

         */

        /*
        itemViewModel.getLiveBoughtItems().observe(requireActivity(), new Observer<ArrayList<Item>>() {
            @Override
            public void onChanged(ArrayList<Item> currentItems) {

                if(currentItems.size() != 0){
                    items[0].clear();
                    items[0].addAll(currentItems);
                    Item displayItem = new Item("select item",0);
                    items[0].add(0,displayItem);

                }


            }
        });

         */




// Creating adapter for spinner
/*

        ArrayAdapter<Report> dataAdapter = new ArrayAdapter<Report>(context
                , android.R.layout.simple_spinner_item, dateViewModel.getReports());
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

 */

/*
// attaching data adapter to spinner and setItemSelected
        dateSpinner.setAdapter(dataAdapter);
        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    Report selected = new Report();
                    selected = (Report) parent.getItemAtPosition(position);//<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                    Log.i(TAG, "onItemSelected: "+selected.getTotal());
                    dateViewModel.setReport(selected);
                }else{
                    dateViewModel.setReport(null);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });


 */
        //ReportRecyclerViewAdapter adapter = new ReportRecyclerViewAdapter();
        //recyclerView.setAdapter(adapter);
/*
        dateViewModel.getLiveReport().observe(requireActivity(), report -> {
            //get report here
            if(report!=null){
                //adapter.setDisplayItems(dateViewModel.findShoppingContent(report.getReport_Id()));
            }
        });

 */

        return view;
    }

}