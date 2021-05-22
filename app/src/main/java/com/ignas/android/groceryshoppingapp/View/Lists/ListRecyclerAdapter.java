package com.ignas.android.groceryshoppingapp.View.Lists;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListRecyclerAdapter extends RecyclerView.Adapter<ListRecyclerAdapter.ViewHolder> {
   private final int MAX_QUANTITY = 9999999;
   private List<Item> mValues = new ArrayList<>();
   private List<Association> aValues = new ArrayList<>();
   private int list_Id=-1;
   private final ItemClickListener mItemClickListener;
   private final String TAG = "log";

//constructor
    public ListRecyclerAdapter(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }
    public interface ItemClickListener{
        void onItemSaveClick(int list_Id,int item_Id,int quantity);
        void onItemRemoveClick(int item_Id);
    }

    //observe update methods
    public void updateViewItems(ArrayList<Item> mValues){
        this.mValues = mValues;
    }

    public void updateListItems(List<Association> aValues,int listID){
        this.aValues = aValues;
        Log.d(TAG, "updateListItems: "+ aValues.size());
        list_Id = listID;

    }

//default methods
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lists_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        Item item = mValues.get(position);

        holder.product_name.setText(item.getItemName());
        if(aValues.size() !=0) {
            Association assoItem = aValues.stream()
                    .filter(value -> item.getItem_id() == value.getItem_Id())
                    .findFirst().orElse(null);

            if(assoItem != null){
                holder.quantity.setText(String.valueOf(assoItem.getQuantity()));
                holder.quantity.setClickable(false);
                toggleBtn(holder,true);
                holder.quantity.setEnabled(false);


            }else{
                toggleBtn(holder,false);
                holder.quantity.setText("");
                holder.quantity.setEnabled(true);
            }
        }else{

            holder.quantity.setText("");
            holder.quantity.setEnabled(true);
            toggleBtn(holder,false);
        }

//add item
        holder.saveBtn.setOnClickListener(vew ->{
            int item_Id = item.getItem_id();
            int quantity=0;

            if(list_Id!=-1){
                String qString = holder.quantity.getText().toString();
                if(!qString.equals("")){
                    quantity = Integer.parseInt(qString);
                }
                if(quantity < MAX_QUANTITY){
                    toggleBtn(holder,true);
                    holder.quantity.setEnabled(false);
                }else{
                    item_Id=-1;
                }

            }
            mItemClickListener.onItemSaveClick(list_Id,item_Id,quantity);


            });



//remove item from list association.----------------------
        holder.deleteBtn.setOnClickListener(vew->{
            if(!holder.quantity.isEnabled()){
                Item itemToRemove = mValues.get(position);
                String quantityText = holder.quantity.getText().toString();
                if(quantityText!=""){
                    holder.quantity.setEnabled(true);
                    holder.quantity.setText("");
                    toggleBtn(holder,false);
                    mItemClickListener.onItemRemoveClick(itemToRemove.getItem_id());
                }else{
                    mItemClickListener.onItemRemoveClick(-1);
                }
            }
        });

        }
    private void toggleBtn(ViewHolder holder,boolean added){
        if(added){
            holder.itemView.setBackgroundResource(R.color.select_color);
            holder.saveBtn.setVisibility(View.INVISIBLE);
            holder.deleteBtn.setVisibility(View.VISIBLE);
        }else{
            holder.itemView.setBackgroundResource(R.color.main);
            holder.saveBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public int getItemCount() {
        return  mValues.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_name;
        EditText quantity;
        FloatingActionButton saveBtn;
        FloatingActionButton deleteBtn;
        public ViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.productName_list_copy);
            quantity = view.findViewById(R.id.quantity_list_copy);
            saveBtn=view.findViewById(R.id.save_list_copy);
            deleteBtn=view.findViewById(R.id.del_list_copy);
        }
    }
}