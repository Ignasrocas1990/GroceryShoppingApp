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

   private List<Item> mValues = new ArrayList<>();
   private ItemClickListener mItemClickListener;
   private String TAG = "log";

    public MyItemRecyclerViewAdapter(ArrayList<Item> items,ItemClickListener itemClickListener){
        this.mValues = items;
        this.mItemClickListener = itemClickListener;
    }

    public MyItemRecyclerViewAdapter(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
    public void updateViewItems(ArrayList<Item> mValues){
        this.mValues = mValues;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mValues.get(position);
        //show the hints
        if(item.getLastingDays()==0){
            holder.lasting_days.setText("");
        }else{
            holder.lasting_days.setText(String.valueOf(item.getLastingDays()));
        }
        if(item.getAmount()==0){
            holder.quantity.setText("");
        }else{
            holder.quantity.setText(String.valueOf(item.getAmount()));
        }
        if(item.getPrice()==0.f){
            holder.price.setText("");
        }else{
            holder.price.setText(String.valueOf(item.getPrice()));
        }
        holder.product_name.setText(item.getItemName());

        holder.saveBtn.setOnClickListener(vew ->{
            String newName = holder.product_name.getText().toString();
            String newDays = holder.lasting_days.getText().toString();
            String newQuantity = holder.quantity.getText().toString();
            String newPrice = holder.price.getText().toString();

            Item itemToAdd = mValues.get(position);
            if (item.getItemName().equals("") && !newName.equals("")) {

                add(itemToAdd,newName,newDays,newQuantity,newPrice);
                mItemClickListener.onItemSaveClick(new Item());
                notifyItemInserted(mValues.size()-1);

            }else if(!newName.equals(item.getItemName()) || Integer.parseInt(newDays)!=item.getLastingDays()
                    || Integer.parseInt(newQuantity) != item.getAmount() || Float.parseFloat(newPrice) != item.getPrice()){

                ;
                mItemClickListener.onItemChangeClick(new Item(newName,Float.parseFloat(newPrice),
                        Integer.parseInt(newDays),Integer.parseInt(newQuantity)));

                Log.i(TAG, "to be updated ");

            }

            //mItemClickListener.onItemSaveClick(itemToAdd);
            });

        holder.deleteBtn.setOnClickListener(vew->{

            Item itemToRemove = mValues.get(position);
            if(!itemToRemove.getItemName().equals("")){
                mItemClickListener.onItemRemoveClick(itemToRemove);
                //removeUpdate(itemToRemove);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,mValues.size());
            }

        });

        }
        public void add(Item item,String newName,String newDays,String newQuantity,String newPrice){
                item.setItemName(newName);
                if(newQuantity.equals("")){
                    item.setAmount(0);
                }else{
                    item.setAmount(Integer.parseInt(newQuantity));
                }
                if(newDays.equals("")){
                    item.setLastingDays(0);
                }else{
                    item.setLastingDays(Integer.parseInt(newDays));
                }
                if(newPrice.equals("")){
                    item.setPrice(0.f);
                }else{
                    item.setPrice(Float.parseFloat(newPrice));
                }
        }
        public void removeUpdate(Item item){
            if(getItemCount()>1){
                mValues.remove(item);
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
        void onItemChangeClick(Item item);
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