package com.ignas.android.groceryshoppingapp.View.Report;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.View.ShoppingDate.DateViewModel;

public class ReportFragment extends Fragment {
    Context context;
    private final String TAG  = "log";


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
        DateViewModel dateViewModel = ViewModelProviders.of(this).get(DateViewModel.class);
        Button viewBtn = view.findViewById(R.id.viewBtn);
        Spinner dateSpinner = view.findViewById(R.id.dateSpinner);
        context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.reportRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
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

        viewBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                dateViewModel.findReportItems();
            }
        });




        return view;
    }

}