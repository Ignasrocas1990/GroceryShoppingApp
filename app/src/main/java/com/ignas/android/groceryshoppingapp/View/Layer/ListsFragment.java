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
import android.widget.EditText;
import android.widget.Toast;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.R;

import java.util.ArrayList;

public class ListsFragment extends Fragment {
    CurListViewModel curListViewModel;
    private static final String TAG = "log";
    public ListsFragment() {
    }

    public static ListsFragment newInstance() {
        ListsFragment fragment = new ListsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.lists_recycler, container, false);
        ViewModel viewModel = ViewModelProviders.of(requireActivity()).get(ViewModel.class);
        curListViewModel = ViewModelProviders.of(requireActivity()).get(CurListViewModel.class);

        RecyclerView rec = view.findViewById(R.id.test_list);
        Context context = view.getContext();
        Button createBtn = view.findViewById(R.id.createList);
        Button deleteBtn = view.findViewById(R.id.delList);
        EditText listName_Box = view.findViewById(R.id.listName);
        EditText shopName_Box = view.findViewById(R.id.shopname);

        curListViewModel.getCurrentList().observe(requireActivity(), new Observer<ItemList>() {
            @Override
            public void onChanged(ItemList itemList) {
                if(itemList == null){
                    listName_Box.setText("");
                    shopName_Box.setText("");
                    rec.setVisibility(View.INVISIBLE);
                }else{
                    listName_Box.setText(itemList.getListName());
                    shopName_Box.setText(itemList.getShopName());
                    rec.setVisibility(View.VISIBLE);
                }

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String listName = listName_Box.getText().toString();
                String shopName = shopName_Box.getText().toString();
                viewModel.removeList(listName,shopName);
                Toast.makeText(context, "List has been removed", Toast.LENGTH_SHORT).show();
            }
        });

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = listName_Box.getText().toString();
                String shopName = shopName_Box.getText().toString();
                if(!listName.equals("")){
                    viewModel.createList(listName,shopName);
                    rec.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "List created,Please add ur items", Toast.LENGTH_SHORT).show();
                }
            }
        });

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