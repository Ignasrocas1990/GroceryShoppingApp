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

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.View.Item.ItemViewModel;
import com.ignas.android.groceryshoppingapp.View.Lists.AssoViewModel;
import com.ignas.android.groceryshoppingapp.View.Lists.ListsViewModel;
import com.ignas.android.groceryshoppingapp.View.ShoppingDate.DateViewModel;

import java.util.ArrayList;

public class ReportFragment extends Fragment {
    private final String TAG  = "log";
    Context context=null;
    ItemViewModel itemViewModel;
    ListsViewModel listViewModel;
    AssoViewModel assoViewModel;
    DateViewModel dateViewModel;
    RecyclerView recyclerView;


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

        //Button viewBtn = view.findViewById(R.id.viewBtn);
        Spinner itemSpinner = view.findViewById(R.id.dateSpinner);
       // final ArrayAdapter[] spinnerAdapter = new ArrayAdapter[1];
        RecyclerView recyclerView = view.findViewById(R.id.reportRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ArrayList<Item> items =  itemViewModel.getBoughtItems();
        items = itemViewModel.setSpinnerText(items);

//set spinner
        ArrayAdapter<Item> itemArrayAdapter = new ArrayAdapter<>(context
                , android.R.layout.simple_spinner_item, items);
        itemArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemArrayAdapter.setNotifyOnChange(true);
        itemSpinner.setAdapter(itemArrayAdapter);

//at selected item in the item spinner
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    Item selectedItem = (Item) parent.getItemAtPosition(position);
                    itemViewModel.itemQuery(selectedItem.getItem_id());
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//set up Recycler View
        ReportRecyclerViewAdapter reportAdapter = new ReportRecyclerViewAdapter(items);
        recyclerView.setAdapter(reportAdapter);

//observer lists queried from database
        itemViewModel.getBoughtLists().observe(requireActivity(), itemLists->{
            reportAdapter.updateValues(itemLists,itemViewModel.getBoughtAssos());
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