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

    private String TAG = "log";

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



    public ReportRecyclerViewAdapter(List<Item> i) {
        items = i;
    }

    public void fromDateSpinner(List<ItemList> dateI,List<Association> dAssos){
        dateItemLists = dateI;
        dateAssos = dAssos;
        dateSpinner = true;
    }
    public void fromItemSpinner(Item item, List<ItemList> l, List<Association> a){
        currentItem = item;
        itemLists =l;
        itemAssos =a;
        itemSpinner=true;
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

        if(itemSpinner && !dateSpinner){//item spinner selected
            Association curAsso = itemAssos.get(position);

            ItemList itemList=null;
            if(curAsso.getList_Id()==0){
                itemList = new ItemList();
            }else{
                itemList = itemLists.stream().
                        filter(list->list.getList_Id()== curAsso.getList_Id()).findFirst().orElse(null);

                if(itemList !=null) {
                    holder.labelNameTextView.setText(R.string.list_name_label);
                    holder.nameTextView.setText(itemList.getListName());
                    holder.shopTextView.setText(itemList.getShopName());
                    holder.priceTextView.setText(String.valueOf(currentItem.getPrice()));
                    holder.quantityTextView.setText(String.valueOf(curAsso.getQuantity()));
                    holder.dateTextView.setText(curAsso.getDisplayDate());
                    holder.dateLabel.setText(R.string.date_report);

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
                holder.nameTextView.setText(R.string.every_list_report);
                holder.shopTextView.setText(R.string.every_shop_report);

            }else{
                holder.nameTextView.setText(itemList.getListName());
                holder.shopTextView.setText(itemList.getShopName());
            }
            holder.priceTextView.setText(String.valueOf(curItem.getPrice()));
            holder.quantityTextView.setText(String.valueOf(curAsso.getQuantity()));
            holder.dateTextView.setText(curItem.getItemName());//item name
            holder.labelNameTextView.setText(R.string.list_name_label);
            holder.dateLabel.setText(R.string.item_name_report);


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
//setters

    public void setItemSpinner(boolean itemSpinner) {
        this.itemSpinner = itemSpinner;
    }

    public void setDateSpinner(boolean dateSpinner) {
        this.dateSpinner = dateSpinner;
    }

    public void setDateAssos(List<Association> dateAssos) {
        this.dateAssos = dateAssos;
        itemSpinner = false;
    }
    public void setItemAssos(List<Association> filteredAssos) {
        this.itemAssos = filteredAssos;
        dateSpinner = false;
    }
}