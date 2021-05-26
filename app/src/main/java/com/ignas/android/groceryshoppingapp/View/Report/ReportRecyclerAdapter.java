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
/***
 * Author:Ignas Rocas
 * Student Id: C00135830
 * Date: 28/05/2021
 * Purpose: Project, Item Recycler adapter controls the data
 * for recycler view component
 */
public class ReportRecyclerAdapter extends RecyclerView.Adapter<ReportRecyclerAdapter.ViewHolder> {

    //date spinner data
   private List<Item> items = new ArrayList<>();
    private List<ItemList> dateItemLists = new ArrayList<>();
    private List<Association> dateAssos = new ArrayList<>();
    private boolean dateSpinner = false;

    //item spinner data
    private Item currentItem;
   private List<ItemList> itemLists = new ArrayList<>();
   private List<Association> itemAssos = new ArrayList<>();
   private boolean itemSpinner = false;



    public ReportRecyclerAdapter(List<Item> i) {
        items = i;
    }
//different data from different spinners
    public void fromDateSpinner(List<ItemList> dateI,List<Association> dAssos){
        dateItemLists = dateI;
        dateAssos = dAssos;
    }
    public void fromItemSpinner(Item item, List<ItemList> l, List<Association> a){
        currentItem = item;
        itemLists =l;
        itemAssos =a;
    }


    @NotNull
    @Override
    public ReportRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item, parent, false);
        return new ReportRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportRecyclerAdapter.ViewHolder holder, int position) {

        if(itemSpinner && !dateSpinner){//item spinner selected
            Association curAsso = itemAssos.get(position);

            ItemList itemList=null;
            if(curAsso.getList_Id()==0){
                itemList = new ItemList();
            }else{
                itemList = itemLists.stream().
                        filter(list->list.getList_Id()== curAsso.getList_Id()).findFirst().orElse(null);

                if(itemList !=null) {
                    holder.labelName.setText("List : ");
                    holder.nameTextView.setText(itemList.getListName());
                    holder.shopTextView.setText(itemList.getShopName());
                    holder.priceTextView.setText(String.valueOf(currentItem.getPrice()));
                    holder.quantityTextView.setText(String.valueOf(curAsso.getQuantity()));
                    holder.dateTextView.setText(curAsso.getDisplayDate());
                    holder.dateLabel.setText("Date");

                }
            }
        }else if(dateSpinner && !itemSpinner){//date spinner selected
            ItemList itemList=null;
            Association curAsso = dateAssos.get(position);
            Item curItem = items.stream()
                    .filter(item->item.getItem_id()==curAsso.getItem_Id()).findFirst().orElse(null);

            if(curItem!=null){
                itemList = dateItemLists.stream()
                    .filter(list->list.getList_Id()==curAsso.getList_Id()).findFirst().orElse(null);
            }
            if(itemList == null){
                holder.nameTextView.setText("all");
                holder.shopTextView.setText("all");

            }else{
                holder.nameTextView.setText(itemList.getListName());
                holder.shopTextView.setText(itemList.getShopName());
            }
            holder.priceTextView.setText(String.valueOf(curItem.getPrice()));
            holder.quantityTextView.setText(String.valueOf(curAsso.getQuantity()));
            holder.dateTextView.setText(curItem.getItemName());//item name
            holder.labelName.setText("List : ");
            holder.dateLabel.setText(" Item ");


        }
    }

    @Override
    public int getItemCount() {
        if (!itemSpinner && dateSpinner) {
            return dateAssos.size();
        } else if (itemSpinner && !dateSpinner) {
            return itemAssos.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameTextView,priceTextView,shopTextView,
                quantityTextView,labelName,dateTextView,dateLabel;

        public ViewHolder(@NonNull @NotNull View view) {
            super(view);
            nameTextView = view.findViewById(R.id.reportName);
            priceTextView = view.findViewById(R.id.reportPrice);
            shopTextView = view.findViewById(R.id.reportShop);
            quantityTextView = view.findViewById(R.id.reportQuantity);
            labelName = view.findViewById(R.id.labelName);
            dateTextView = view.findViewById(R.id.reportDate);
            dateLabel = view.findViewById(R.id.dateLabel);
        }
    }
//setters

    public void setItemSpinner(boolean itemSpinner) {
        this.itemSpinner = itemSpinner;
    }

    public void setDateSpinner(boolean dateSpinner) {
        this.dateSpinner = dateSpinner;
    }

    public void setDateAssos(List<Association> dateAssos) {
        this.dateAssos = dateAssos;
    }
    public void setItemAssos(List<Association> filteredAssos) {
        this.itemAssos = filteredAssos;
    }
}