package com.ignas.android.groceryshoppingapp.View.Report;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<ReportRecyclerViewAdapter.ViewHolder> {

   private List<Item> items = new ArrayList<>();
   private List<ItemList> lists = new ArrayList<>();
   private List<Association> assos = new ArrayList<>();
   private String TAG = "log";

    public ReportRecyclerViewAdapter(List<Item> i) {
        items = i;
    }

    public void updateValues(List<ItemList> l,List<Association> a){
        lists=l;
        assos=a;
    }

    @NotNull
    @Override
    public ReportRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item, parent, false);
        return new ReportRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportRecyclerViewAdapter.ViewHolder holder, int position) {

        //holder.dateTextView.setText(iValues.get(position));
    }

    @Override
    public int getItemCount() {
        return assos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView;

        public ViewHolder(@NonNull @NotNull View view) {
            super(view);
            nameTextView = view.findViewById(R.id.reportName);
        }
    }
}