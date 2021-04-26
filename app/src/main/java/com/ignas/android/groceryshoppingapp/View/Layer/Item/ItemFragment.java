package com.ignas.android.groceryshoppingapp.View.Layer.Item;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;

import java.util.ArrayList;

public class ItemFragment extends Fragment {
    public final String TAG = "log";

    public ItemFragment() {}

    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    EditText product_name,lasting_days,price;
    FloatingActionButton saveBtn,deleteBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.item_recycler, container, false);
        Context context = view.getContext();
        ItemViewModel itemViewModel = ViewModelProviders.of(requireActivity()).get(ItemViewModel.class);
        final int[] curPosition = new int[1];
        curPosition[0]=-1;

        saveBtn=view.findViewById(R.id.saveFab);
        deleteBtn=view.findViewById(R.id.delFab);
        product_name = view.findViewById(R.id.cur_productName);
        lasting_days = view.findViewById(R.id.cur_lastingDays);
        price = view.findViewById(R.id.cur_price);


        // Set the adapter
        RecyclerView recyclerView = view.findViewById(R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(new ItemRecyclerViewAdapter.ItemClickListener() {
                @Override
                public void onItemClick(int position, String newName, String newDays, String newPrice) {
                    product_name.setText(newName);
                    price.setText(newPrice);
                    lasting_days.setText(newDays);
                    curPosition[0] = position;
                }
            });
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String stringName = product_name.getText().toString();
                String stringDays = lasting_days.getText().toString();
                String stringPrice = price.getText().toString();
                if(stringName.equals("")){
                    Toast.makeText(context, "List needs a name", Toast.LENGTH_SHORT).show();
                }else if (curPosition[0] == -1){
                    itemViewModel.addItem(stringName,stringDays,stringPrice);
                    curPosition[0] = -1;
                    product_name.setText("");
                    lasting_days.setText("");
                    price.setText("");
                    Toast.makeText(context, "Item is created", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemInserted(adapter.getItemCount()-1);
                }else{
                    itemViewModel.changeItem(curPosition[0],stringName,stringDays,stringPrice);
                    Toast.makeText(context, "Item is changed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(curPosition[0] != -1){
    //TODO ---------------------------------------------------Check here all the lists that the item is associated to it.
                    itemViewModel.removeItem(curPosition[0]);
                    adapter.notifyItemRemoved(curPosition[0]);
                    adapter.notifyItemRangeChanged(curPosition[0],adapter.getItemCount()-1);

                    curPosition[0] = -1;
                    product_name.setText("");
                    lasting_days.setText("");
                    price.setText("");
                    Toast.makeText(context, "Item is created", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context, "No item selected", Toast.LENGTH_SHORT).show();
                }
            }
        });

            recyclerView.setAdapter(adapter);
            itemViewModel.getLiveItems().observe(requireActivity(), new Observer<ArrayList<Item>>() {
                @Override
                public void onChanged(ArrayList<Item> items) {

                    adapter.updateViewItems(items);
                }
            });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_items,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.delete_menu_btn){
            Toast.makeText(getActivity(), "delete selected", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}