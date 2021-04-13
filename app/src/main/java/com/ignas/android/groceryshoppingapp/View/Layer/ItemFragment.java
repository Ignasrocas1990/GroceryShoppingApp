package com.ignas.android.groceryshoppingapp.View.Layer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ignas.android.groceryshoppingapp.Logic.PageViewModel;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {
    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    public final String TAG = "log";
    ArrayList<Item> list;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {}
    public ItemFragment(ArrayList<Item> list){
        this.list = list;
    }

    // TODO: Customize parameter initialization
    public static ItemFragment newInstance(ArrayList<Item> list) {
        ItemFragment fragment = new ItemFragment(list);
        //Bundle args = new Bundle();
        //args.putInt(ARG_COLUMN_COUNT, columnCount);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }

         */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {

            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(list, new MyItemRecyclerViewAdapter.ItemClickListener() {
                @Override
                public void onItemSaveClick(Item item) {
                    Log.d(TAG, "onItemSaveClick: "+list.size());
                    Log.d(TAG, "Save Item: "+ item.getItem_id()+" "+item.getItemName()+" "+item.getLastingDays()+" "+item.getAmount()+" "+item.getPrice());

                }

                @Override
                public void onItemRemoveClick(Item item) {
                    Log.d(TAG, "onItemSaveClick: "+list.size());
                    Log.d(TAG, "removed Item: "+ item.getItem_id()+" "+item.getItemName()+" "+item.getLastingDays()+" "+item.getAmount()+" "+item.getPrice());
                }
            }));
        }
        return view;
    }
}