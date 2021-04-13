package com.ignas.android.groceryshoppingapp.View.Layer;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

   private List<Item> mValues;
   private ItemClickListener mItemClickListener;
   private String TAG = "log";

/*
    public MyItemRecyclerViewAdapter(List<DummyItem> items) {
        mValues = items;
    }
 */
    public MyItemRecyclerViewAdapter(ArrayList<Item> items,ItemClickListener itemClickListener){
        /*
        data = new PageViewModel();
        data.createEmptyList();
        list = data.getEmptyList();

         */

        this.mValues = items;
        this.mItemClickListener = itemClickListener;
    }
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {//need to fix if 0 then its empty space (so hints seen)
        Item item = mValues.get(position);
        holder.product_name.setText(item.getItemName());
        holder.lasting_days.setText(String.valueOf(item.getLastingDays()));
        holder.quantity.setText(String.valueOf(item.getAmount()));
        holder.price.setText(String.valueOf(item.getPrice()));


        holder.saveBtn.setOnClickListener(vew ->{
            String newName = holder.product_name.getText().toString();
            String newDays = holder.lasting_days.getText().toString();
            String newQuantity = holder.quantity.getText().toString();
            String newPrice = holder.price.getText().toString();

            Item itemToAdd = mValues.get(position);
            if (item.getItemName().equals("") && !newName.equals("")) {
                        add(itemToAdd,holder,newName,newDays,newQuantity,newPrice);
            }else if(!newName.equals(item.getItemName()) || Integer.parseInt(newDays)!=item.getLastingDays()
                    || Integer.parseInt(newQuantity) != item.getAmount() || Float.parseFloat(newPrice) != item.getPrice()){

                Log.d(TAG, "to be updated ");

            }

            mItemClickListener.onItemSaveClick(itemToAdd);
            });

        holder.deleteBtn.setOnClickListener(vew->{

            Item itemToRemove = mValues.get(position);
            if(!itemToRemove.getItemName().equals("")){
                mItemClickListener.onItemRemoveClick(itemToRemove);
                removeUpdate(position,itemToRemove);
            }

        });

        }
        public void add(Item item, ViewHolder holder,String newName,String newDays,String newQuantity,String newPrice){
                item.setItemName(newName);
                item.setAmount(Integer.parseInt(newQuantity));
                item.setLastingDays(Integer.parseInt(newDays));
                item.setPrice(Float.parseFloat(newPrice));

                mValues.add(new Item());
                notifyItemInserted(mValues.size()-1);
        }
        public void removeUpdate(int position,Item item){
        Item itemToRemove = new Item();
            if(getItemCount()>1){
                item.setItemName("");
                item.setAmount(0);
                item.setLastingDays(0);
                item.setPrice(0.f);
                mValues.remove(item);
                notifyItemRemoved(position);
                //notifyItemRemoved(mValues.size()-1);
            }else{
                item.setItemName("");
                item.setAmount(0);
                item.setLastingDays(0);
                item.setPrice(0.f);
            }
        }

    public interface ItemClickListener{
        void onItemSaveClick(Item item);
        void onItemRemoveClick(Item item);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        EditText product_name,lasting_days,quantity,price;
        FloatingActionButton saveBtn;
        FloatingActionButton deleteBtn;
        public ViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.productName);
            lasting_days = view.findViewById(R.id.lastingDays);
            quantity = view.findViewById(R.id.quantity);
            price = view.findViewById(R.id.price);
            saveBtn=view.findViewById(R.id.saveFab);
            deleteBtn=view.findViewById(R.id.delFab);
        }
    }
}