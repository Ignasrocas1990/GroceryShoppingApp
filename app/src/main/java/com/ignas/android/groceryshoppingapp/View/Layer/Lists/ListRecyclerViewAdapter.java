package com.ignas.android.groceryshoppingapp.View.Layer.Lists;

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

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {

   private List<Item> mValues = new ArrayList<>();
   private List<Association> aValues = new ArrayList<>();
   private int list_Id=-1;
   private final ItemClickListener mItemClickListener;
   private final String TAG = "log";

//constructor
    public ListRecyclerViewAdapter(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }
    public interface ItemClickListener{
        void onItemSaveClick(int list_Id,int item_Id,int quantity);
        void onItemRemoveClick(Item item);
    }

    //observe update methods
    public void updateViewItems(ArrayList<Item> mValues){
        this.mValues = mValues;
    }

    public void updateListItems(ArrayList<Association> aValues){
        this.aValues = aValues;
    }
    public void updateListId(int list_Id){
        this.list_Id = list_Id;
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
        if(aValues.size() !=0) {
            Association assoItem = aValues.stream()
                    .filter(value -> item.getItem_id() == value.getItem_Id())
                    .findFirst().orElse(null);
            if(assoItem != null){
                holder.quantity.setText(String.valueOf(assoItem.getQuantity()));
                holder.quantity.setClickable(false);
                holder.deleteBtn.setVisibility(View.VISIBLE);
                holder.saveBtn.setVisibility(View.INVISIBLE);

            }else{
                holder.product_name.setText(item.getItemName());
                holder.quantity.setText("");
            }
        }else{
            holder.quantity.setText("");
            holder.product_name.setText(item.getItemName());
        }


        holder.saveBtn.setOnClickListener(vew ->{
            int item_Id = item.getItem_id();
            int quantity=0;

            //add----
            if(list_Id!=-1){
                holder.deleteBtn.setVisibility(View.VISIBLE);
                holder.saveBtn.setVisibility(View.INVISIBLE);
                holder.quantity.setClickable(false);

                String qString = holder.quantity.getText().toString();
                if(!qString.equals("")){
                    quantity = Integer.parseInt(qString);
                }
            }
            mItemClickListener.onItemSaveClick(list_Id,item_Id,quantity);
            });

        holder.deleteBtn.setOnClickListener(vew->{
            holder.saveBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setVisibility(View.INVISIBLE);

            Item itemToRemove = mValues.get(position);

            mItemClickListener.onItemRemoveClick(itemToRemove);
        });

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