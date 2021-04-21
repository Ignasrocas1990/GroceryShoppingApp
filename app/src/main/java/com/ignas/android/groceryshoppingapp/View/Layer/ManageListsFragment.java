package com.ignas.android.groceryshoppingapp.View.Layer;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.R;

import java.util.ArrayList;

public class ManageListsFragment extends Fragment {

    private static final String TAG = "log";
    private ArrayList<ItemList> app_lists;
    private ArrayList<Item> app_items;


    public ManageListsFragment() {
    }

    public ManageListsFragment(ArrayList<ItemList> app_lists, ArrayList<Item> items) {
        this.app_lists = app_lists;
        app_items = items;
    }

    // TODO: Rename and change types and number of parameters
    public static ManageListsFragment newInstance(ArrayList<ItemList> app_lists, ArrayList<Item> items) {
        ManageListsFragment fragment = new ManageListsFragment(app_lists,items);
        //Bundle args = new Bundle();
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
        //}
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_manage_lists, container, false);
        Button btn = view.findViewById(R.id.CreateList);

        RecyclerView rec = view.findViewById(R.id.test_list);


        //TODO ----------------------------------------------------VV|VV maybe passdown the list instead of get from here
        Context context = view.getContext();
        rec.setLayoutManager(new LinearLayoutManager(context));
        ListRecyclerViewAdapter adapter = new ListRecyclerViewAdapter(new ListRecyclerViewAdapter.ItemClickListener() {

            @Override
            public void onItemSaveClick(Item item) {

            }

            @Override
            public void onItemRemoveClick(Item item) {

            }
        });
        rec.setAdapter(adapter);
        ViewModel viewModel = ViewModelProviders.of(requireActivity()).get(ViewModel.class);
        viewModel.getLiveItems().observe(requireActivity(), new Observer<ArrayList<Item>>() {
            @Override
            public void onChanged(ArrayList<Item> items) {
                adapter.updateViewItems(items);
                adapter.notifyDataSetChanged();
                Log.i(TAG, "onChanged: message");
            }
        });


        return view;
    }
}