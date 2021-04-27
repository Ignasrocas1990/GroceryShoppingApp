package com.ignas.android.groceryshoppingapp.View.Layer.Item;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ItemRecyclerViewAdapter extends RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder> {

   private List<Item> mValues = new ArrayList<>();
   private final ItemClickListener mItemClickListener;
   View prevSelected = null;
   private String TAG = "log";

    public ItemRecyclerViewAdapter(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }
    public void updateViewItems(ArrayList<Item> mValues){
        this.mValues = mValues;
        notifyDataSetChanged();
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Item item = mValues.get(position);
        holder.itemView.setBackgroundResource(R.color.blue);
        holder.product_name.setText(item.getItemName());
        holder.lasting_days.setText(String.valueOf(item.getLastingDays()));
        holder.price.setText(String.valueOf(item.getPrice()));

        holder.itemView.setOnClickListener(vew ->{
            String newName="",newDays="",newPrice="";
            if(prevSelected==null){
                prevSelected=holder.itemView;
                holder.itemView.setBackgroundResource(R.color.red);
                 newName = holder.product_name.getText().toString();
                 newDays = holder.lasting_days.getText().toString();
                 newPrice = holder.price.getText().toString();
                mItemClickListener.onItemClick(position,newName,newDays,newPrice);
            }else if(prevSelected.equals(holder.itemView)){

                prevSelected=null;
                holder.itemView.setBackgroundResource(R.color.blue);
                mItemClickListener.onItemClick(-1,"","","");

            }else{
                prevSelected.setBackgroundResource(R.color.blue);
                prevSelected=holder.itemView;
                holder.itemView.setBackgroundResource(R.color.red);

                newName = holder.product_name.getText().toString();
                newDays = holder.lasting_days.getText().toString();
                newPrice = holder.price.getText().toString();
                mItemClickListener.onItemClick(position,newName,newDays,newPrice);
            }
            });
        }
    public interface ItemClickListener{
        void onItemClick(int position,String newName,String newDays,String newPrice);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView product_name,lasting_days,price;
        public ViewHolder(View view) {
            super(view);
            product_name = view.findViewById(R.id.productName);
            lasting_days = view.findViewById(R.id.lastingDays);
            price = view.findViewById(R.id.price);

        }
    }
}