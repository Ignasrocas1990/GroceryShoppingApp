package com.ignas.android.groceryshoppingapp.View.Report;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.Report;
import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.View.Item.ItemRecyclerViewAdapter;
import com.ignas.android.groceryshoppingapp.View.ShoppingDate.DateViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class ReportFragment extends Fragment {
    Context context;


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
        Button viewBtn = view.findViewById(R.id.viewBtn);
        context = view.getContext();
        DateViewModel dateViewModel = ViewModelProviders.of(this).get(DateViewModel.class);
        RecyclerView recyclerView = view.findViewById(R.id.reportRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Report> reports = dateViewModel.getReports();
                for(Report r:reports){
                    Log.i("log", "onCreateView: "+r.getTotal());//<----- working
                }
                if(reports.size()==0){
                    Log.i("log", "onClick: empty");
                }
            }
        });



//TODO to be deleted vvv   -------------

        HashMap<String,ArrayList<String>> dummyMap = new HashMap<>();
        ArrayList<String> dummyList = new ArrayList<>();
        ArrayList<String> temp=null;
        for(int i = 0;i<5;i++){
            String date = i+""+i+"/"+i+""+i+"/"+"iiii";
            temp = new ArrayList<String>();
            for(int x=i;x<i+3;x++){
                temp.add(String.valueOf(x));
            }
            dummyList.add(date);
            dummyMap.put(date,temp);
        }
//-------------------

        ReportRecyclerViewAdapter adapter = new ReportRecyclerViewAdapter(dummyList,context,new ReportRecyclerViewAdapter.DateClickListener() {
            @Override
            public ArrayList<String> getItems(String s) {
                if(dummyMap.containsKey(s)){
                    return dummyMap.get(s);
                }
                return null;
            }

        });
        recyclerView.setAdapter(adapter);
        return view;
    }
}