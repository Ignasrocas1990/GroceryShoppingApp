package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class ShoppingRecyclerAdapter extends RecyclerView.Adapter<ShoppingRecyclerAdapter.ViewHolder> {

    ArrayList<Item> mItems;


    public void setItems(ArrayList<Item> items) {
        mItems = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //TODO -- Calculate leftover time..----------------------(1)
        Item currentItem = mItems.get(position);
       holder.name.setText(currentItem.getItemName());
       //TODO -- need to get price form Asso's-------------------(after)
       if(Float.compare(currentItem.getPrice(),0.f)==0){
           holder.price.setText(R.string.no_price_found);
       }else{
           holder.price.setText(String.valueOf(currentItem.getPrice()));
       }

    }

    @Override
    public int getItemCount() {
        return 0;
    }




    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,leftOver, amount,list,shop;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.shoppingName);
            amount = itemView.findViewById(R.id.shoppingAmount);
            leftOver = itemView.findViewById(R.id.shopLeftover);
            price = itemView.findViewById(R.id.shoppingPrice);
            list = itemView.findViewById(R.id.shoppingList);
            shop = itemView.findViewById(R.id.shoppingShop);
        }
    }
}
