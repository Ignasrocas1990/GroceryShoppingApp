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
   private Item currentItem;
   private List<ItemList> lists = new ArrayList<>();
   private List<Association> assos = new ArrayList<>();
   private String TAG = "log";

    public ReportRecyclerViewAdapter(List<Item> i) {
        items = i;
    }

    public void updateValues(Item item, List<ItemList> l, List<Association> a){
        currentItem = item;
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
        Association curAsso = assos.get(position);
        ItemList itemList=null;
        if(curAsso.getList_Id()==0){
            itemList = new ItemList();
        }else{
             itemList = lists.stream().
                    filter(list->list.getList_Id()==curAsso.getList_Id()).findFirst().orElse(null);
        }


        if(itemList !=null) {
            holder.labelNameTextView.setText(R.string.list_name_label);
            holder.nameTextView.setText(itemList.getListName());
            holder.shopTextView.setText(itemList.getShopName());
            holder.priceTextView.setText(String.valueOf(currentItem.getPrice()));
            holder.quantityTextView.setText(String.valueOf(curAsso.getQuantity()));
            holder.dateTextView.setText(curAsso.getDisplayDate());
        }
    }

    @Override
    public int getItemCount() {
        return assos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView,priceTextView,shopTextView,
                quantityTextView,labelNameTextView,dateTextView,dateLabel;

        public ViewHolder(@NonNull @NotNull View view) {
            super(view);
            nameTextView = view.findViewById(R.id.reportName);
            priceTextView = view.findViewById(R.id.reportPrice);
            shopTextView = view.findViewById(R.id.reportShop);
            quantityTextView = view.findViewById(R.id.reportQuantity);
            labelNameTextView = view.findViewById(R.id.labelName);
            dateTextView = view.findViewById(R.id.reportDate);
            dateLabel = view.findViewById(R.id.dateLabel);
        }
    }
}