package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShoppingRecyclerAdapter extends RecyclerView.Adapter<ShoppingRecyclerAdapter.ViewHolder> {

    private static final String TAG = "log";
    private List<Item> mItems = new ArrayList<>();
    private final textChangeListener mTextChangeListener;

    public ShoppingRecyclerAdapter(textChangeListener mTextChangeListener) {

        this.mTextChangeListener = mTextChangeListener;
    }

    public void setItems(ArrayList<Item> items) {
        Log.i(TAG, "setItems: "+items.size());
        mItems = items;
    }

    public interface textChangeListener {
        int getLeftOver(Item item);
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
        Item currentItem = mItems.get(position);

        holder.name.setText(currentItem.getItemName());
        if(Float.compare(currentItem.getPrice(),0.f)==0){
           holder.price.setText(R.string.no_price_found);
        }else{
           holder.price.setText(String.valueOf(currentItem.getPrice()));
        }

        int itemLeftOver = mTextChangeListener.getLeftOver(currentItem);

        if(itemLeftOver<0){
            holder.leftOver.setText("0%");
        }else{
            holder.leftOver.setText(itemLeftOver+" % ");
        }

        //TODO -- need to get price form Asso's------Amount & list ID-----------next
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price,leftOver, amount,list,shop;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.shoppingName);
            amount = view.findViewById(R.id.shoppingAmount);
            leftOver = view.findViewById(R.id.shopLeftover);
            price = view.findViewById(R.id.shoppingPrice);
            list = view.findViewById(R.id.shoppingList);
            shop = view.findViewById(R.id.shoppingShop);
        }
    }
}
