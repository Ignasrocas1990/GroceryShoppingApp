package com.ignas.android.groceryshoppingapp.View.Layer;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.View.Layer.dummy.Content.DummyItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

   private List<Item> mValues;
   private ItemClickListener mItemClickListener;
   private String TAG = "Saving";

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
        items.add(new Item());
        this.mValues = items;
        this.mItemClickListener = itemClickListener;
    }
    /*
    public ArrayList<Item> emptySpace(ArrayList<Item> items){
        if (items.size() == 0) {
            items.add(new Item());
        }
        return items;
    }

     */


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
            Item itemToAdd = mValues.get(position);

            if (!holder.product_name.getText().toString().equals("")) {

                        addUpdate(position,holder);
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
        public void addUpdate(int position, ViewHolder holder){
            Item item = mValues.get(position);
            String product_name = holder.product_name.getText().toString();
            String amountString = holder.quantity.getText().toString();
            String lastingDaysString = holder.lasting_days.getText().toString();
            String priceString = holder.price.getText().toString();
            if(!product_name.equals("")){
                item.setItemName(product_name);
                if(amountString.equals("")){
                    item.setAmount(0);
                }else{
                    item.setAmount(Integer.parseInt(amountString));
                }
                if(lastingDaysString.equals("")){
                    item.setLastingDays(0);
                }else{
                    item.setLastingDays(Integer.parseInt(lastingDaysString));
                }
                if(priceString.equals("")){
                    item.setPrice(0.f);
                }else{
                    item.setPrice(Float.parseFloat(priceString));
                }
                position++;
                mValues.add(position,new Item());
                notifyItemInserted(mValues.size()-1);
            }

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