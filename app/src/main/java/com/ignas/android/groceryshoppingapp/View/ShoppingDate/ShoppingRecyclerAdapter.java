package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ShoppingRecyclerAdapter extends RecyclerView.Adapter<ShoppingRecyclerAdapter.ViewHolder> {

    private static final String TAG = "log";
    private List<Item> ntfItems = new ArrayList<Item>();
    private List<Association> assos = new ArrayList<>();
    private final onItemClickListener mOnItemClickListener;

    public ShoppingRecyclerAdapter(ArrayList<Item> notifiedItems, onItemClickListener listener) {
        ntfItems = notifiedItems;
        this.mOnItemClickListener = listener;
    }


    //set basic item info
    public void setItems(ArrayList<Association> itemAssos) {
        assos = itemAssos;
    }

    public interface onItemClickListener {
        void onItemBuy(Item ItemAsso, Association currentAsso, boolean b);
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        Association currentAsso = assos.get(position);
        final boolean[] deleteFlag = {false};
        Item foundItem = ntfItems.stream()
                .filter(item->item.getItem_id()==currentAsso.getItem_Id()).findFirst().orElse(null);

        if(foundItem != null){
            holder.name.setText(foundItem.getItemName());
            holder.price.setText(String.valueOf(foundItem.getPrice()));
            holder.amount.setText(String.valueOf(currentAsso.getQuantity()));
        }
        holder.itemView.setOnClickListener(v -> {
            if(assos.size() == 1){
                deleteFlag[0] = true;
            }
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,getItemCount()-1);
            mOnItemClickListener.onItemBuy(foundItem,currentAsso, deleteFlag[0]);
        });



/*
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


 */
        //TODO -- need to get price form Asso's------Amount & list ID-----------next
    }

    @Override
    public int getItemCount() {
        if(assos!=null){
            return assos.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price, amount;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.shoppingName);
            amount = view.findViewById(R.id.shoppingAmount);
            price = view.findViewById(R.id.shoppingPrice);
        }
    }
}
