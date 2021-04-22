package com.ignas.android.groceryshoppingapp.View.Layer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListRecyclerViewAdapter extends RecyclerView.Adapter<ListRecyclerViewAdapter.ViewHolder> {

   private List<Item> mValues = new ArrayList<>();
   private ItemClickListener mItemClickListener;
   private String TAG = "log";

    public ListRecyclerViewAdapter(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }
    public void updateViewItems(ArrayList<Item> mValues){
        this.mValues = mValues;
    }
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lists_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//need to fix if 0 then its empty space (so hints seen)

        Item item = mValues.get(position);
        holder.product_name.setText(item.getItemName());
        holder.quantity.setText(String.valueOf(item.getAmount()));



        holder.saveBtn.setOnClickListener(vew ->{
            holder.deleteBtn.setVisibility(View.VISIBLE);
            holder.saveBtn.setVisibility(View.INVISIBLE);

            //String newQuantity = holder.quantity.getText().toString();
            Item itemToAdd = mValues.get(position);

            mItemClickListener.onItemSaveClick(itemToAdd);
            });

        holder.deleteBtn.setOnClickListener(vew->{
            holder.saveBtn.setVisibility(View.VISIBLE);
            holder.deleteBtn.setVisibility(View.INVISIBLE);

            Item itemToRemove = mValues.get(position);
            mItemClickListener.onItemRemoveClick(itemToRemove);
        });

        }

    public interface ItemClickListener{
        void onItemSaveClick(Item item);
        void onItemRemoveClick(Item item);
    }

    @Override
    public int getItemCount() {
        return  mValues.size()-1;
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