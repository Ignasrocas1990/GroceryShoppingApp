package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ignas.android.groceryshoppingapp.Models.ShoppingItem;
import com.ignas.android.groceryshoppingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShoppingRecyclerAdapter extends RecyclerView.Adapter<ShoppingRecyclerAdapter.ViewHolder> {

    private static final String TAG = "log";
    private List<ShoppingItem> mItems ;
    private final onItemClickListener mOnItemClickListener;

    public ShoppingRecyclerAdapter(onItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
//set basic item info
    public void setItems(ArrayList<ShoppingItem> items) {
        mItems = items;
    }

    public interface onItemClickListener {
        void onItemBuy(ShoppingItem item);
        void onCancel(ShoppingItem item);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ShoppingItem currentItem = mItems.get(position);

        if(currentItem.isSelected()){
            holder.itemView.setBackgroundResource(R.color.red);
        }else{
            holder.itemView.setBackgroundResource(R.color.blue);
        }

            holder.name.setText(currentItem.getName());
            if(Float.compare(currentItem.getPrice(),0.f)!=0){
                holder.price.setText(String.valueOf(currentItem.getPrice()));
            }
            if(currentItem.getQuantity()!=0){
                holder.amount.setText(String.valueOf(currentItem.getQuantity()));
            }
            if(!currentItem.getListName().equals("")){
                holder.list.setText(currentItem.getListName());
            }
            if(!currentItem.getShopName().equals("")){
                holder.list.setText(currentItem.getShopName());
            }
//item been selected
        holder.itemView.setOnClickListener(v -> {
            if(!currentItem.isSelected()){
                currentItem.setSelected(true);
                holder.itemView.setBackgroundResource(R.color.red);
                mOnItemClickListener.onItemBuy(currentItem);
            }else{
                currentItem.setSelected(false);
                holder.itemView.setBackgroundResource(R.color.blue);
                mOnItemClickListener.onCancel(currentItem);
            }
        });

        //TODO -- need to get price form Asso's------Amount & list ID-----------next
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price, amount,list,shop;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.shoppingName);
            amount = view.findViewById(R.id.shoppingAmount);
            price = view.findViewById(R.id.shoppingPrice);
            list = view.findViewById(R.id.shoppingList);
            shop = view.findViewById(R.id.shoppingShop);
        }
    }
}
