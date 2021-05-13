package com.ignas.android.groceryshoppingapp.View.Report;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Models.ShoppingItem;
import com.ignas.android.groceryshoppingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<ReportRecyclerViewAdapter.ViewHolder> {

   private List<ShoppingItem> iValues = new ArrayList<>();
   private String TAG = "log";

    public ReportRecyclerViewAdapter() {}

    public void setItems(ArrayList<ShoppingItem> items){
        iValues = items;
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
        return iValues.size();
    }

    public void setDisplayItems(ArrayList<ShoppingItem> shoppingContent) {
        iValues = shoppingContent;
        Log.wtf(TAG, "setDisplayItems: "+shoppingContent.size());

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView dateTextView;

        public ViewHolder(@NonNull @NotNull View view) {
            super(view);
            dateTextView = view.findViewById(R.id.reportDateView);
        }
    }
}