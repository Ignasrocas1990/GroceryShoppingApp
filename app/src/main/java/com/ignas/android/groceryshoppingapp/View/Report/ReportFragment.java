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
    Context context=null;
    ItemViewModel itemViewModel;
    ListsViewModel listViewModel;
    AssoViewModel assoViewModel;
    DateViewModel dateViewModel;
    RecyclerView recyclerView;
    Spinner itemSpinner,dateSpinner;
    boolean itemON = false;
    boolean dateON = false;
    Item selectedItem = new Item();
    List<Association> dateAssos = new ArrayList<>();
    ArrayList<String> displayDates = new ArrayList<>();


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
//item drop down resources
        ArrayList<Item> items =  itemViewModel.getBoughtItems();
        items = itemViewModel.setSpinnerText(items);


        //set up Recycler View
        ReportRecyclerViewAdapter reportAdapter = new ReportRecyclerViewAdapter(items);
        recyclerView.setAdapter(reportAdapter);

//set up date SPINNER(Drop down menu)
        HashMap<String, List<Association>> dateGroupAssos = assoViewModel.findAssosByDate();

//observer for date drop down
        assoViewModel.getDateDisplay().observe(requireActivity(),
                strings -> displayDates = strings);

        ArrayAdapter<String> dateArrayAdapter = new ArrayAdapter<>(context
                , android.R.layout.simple_spinner_item, displayDates);
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
                        dateAssos = dateGroupAssos.get(date);
                    }
                    if (itemON) {//find filtered (displaying items)
                        reportAdapter.setDateSpinner(false);
                        reportAdapter.setItemSpinner(true);
                        reportAdapter.setItemAssos(assoViewModel.getCommon(itemViewModel.getBoughtAssos(),dateAssos));

                    }else{//find associations without filter (display dates)
                        reportAdapter.setDateSpinner(true);
                        reportAdapter.setItemSpinner(false);
                        reportAdapter.fromDateSpinner(listViewModel.findLists(dateAssos), dateAssos);
                    }
                    dateON = true;

                }else if(itemON) { //date de-selected but items still selected
                    reportAdapter.setDateSpinner(false);
                    itemViewModel.itemQuery(selectedItem.getItem_id());
                    dateON = false;

                }else{
                    reportAdapter.setDateSpinner(false);
                    dateON = false;

                }
                reportAdapter.notifyDataSetChanged();
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



//at selected item in the item SPINNER
        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0) {
                    selectedItem = (Item) parent.getItemAtPosition(position);

                    if(dateON){//filter by date
                        List<Association> filteredAssos = assoViewModel.filterAssos(dateAssos, selectedItem);
                        reportAdapter.setDateAssos(filteredAssos);
                        reportAdapter.setItemSpinner(false);
                        reportAdapter.setDateSpinner(true);

                    }else{//find assos with no filter
                        reportAdapter.setDateSpinner(false);
                        itemViewModel.itemQuery(selectedItem.getItem_id());
                    }
                    itemON = true;


                }else if(dateON){//find associations from date spinner

                    reportAdapter.setItemSpinner(false);
                    reportAdapter.setDateSpinner(true);
                    reportAdapter.fromDateSpinner(listViewModel.findLists(dateAssos), dateAssos);

                    itemON = false;
                }else{
                    reportAdapter.setItemSpinner(false);
                    itemON = false;

                }
                reportAdapter.notifyDataSetChanged();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//observer lists of ITEMS selected from spinner
        itemViewModel.getBoughtLists().observe(requireActivity(), itemLists->{
            reportAdapter.fromItemSpinner(selectedItem,itemLists, itemViewModel.getBoughtAssos());
            reportAdapter.setItemSpinner(true);
            reportAdapter.notifyDataSetChanged();

        });

        return view;
    }

}