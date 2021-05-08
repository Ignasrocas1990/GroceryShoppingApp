package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShoppingRecyclerAdapter extends RecyclerView.Adapter<ShoppingRecyclerAdapter.ViewHolder> {

    private static final String TAG = "log";
    private List<Item> mItems ;//= new ArrayList<>();
    private List<Association> mAssos ;//= new ArrayList<>();
    private List<ItemList> mLists;
    private final onItemClickListner mOnItemClickListener;

    public ShoppingRecyclerAdapter(ArrayList<Item> items, ArrayList<Association> assos, ArrayList<ItemList> lists, onItemClickListner mOnItemClickListener) {
        mAssos = assos;
        mLists = lists;
        mItems = items;
        this.mOnItemClickListener = mOnItemClickListener;
    }
//set basic item info
    public void setItems(ArrayList<Item> items) {
        mItems = items;
    }

    public interface onItemClickListner {
        void onItemClick(Item item);
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
        Association currAsso = mAssos.get(position);
        Item currItem = mItems.stream().filter(item->item.getItem_id()==currAsso.getItem_Id())
                .findFirst().orElse(null);
        ItemList currList = mLists.stream().filter(list->list.getList_Id()==currAsso.getList_Id())
                .findFirst().orElse(null);

        if(currItem !=null && currList !=null){
            holder.name.setText(currItem.getItemName());
            if(Float.compare(currItem.getPrice(),0.f)!=0){
                holder.price.setText(String.valueOf(currItem.getPrice()));
            }
            if(currAsso.getQuantity()!=0){
                holder.price.setText(String.valueOf(currAsso.getQuantity()));
            }
            holder.list.setText(currList.getListName());
            if(currList.getShopName().equals("")){
                holder.shop.setText(currList.getShopName());
            }
        }


        //mOnItemClickListener.onItemClick(currentItem);

        //TODO -- need to get price form Asso's------Amount & list ID-----------next
    }

    @Override
    public int getItemCount() {
        return mAssos.size();
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
