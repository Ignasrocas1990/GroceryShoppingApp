package com.ignas.android.groceryshoppingapp.View.Layer.Lists;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.View.Layer.Item.AssoViewModel;
import com.ignas.android.groceryshoppingapp.View.Layer.Item.ItemViewModel;

public class ListsFragment extends Fragment {
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
        ListsViewModel listsViewModel = ViewModelProviders.of(requireActivity()).get(ListsViewModel.class);
        ItemViewModel itemViewModel = ViewModelProviders.of(requireActivity()).get(ItemViewModel.class);
        AssoViewModel assoViewModel = ViewModelProviders.of(this).get(AssoViewModel.class);


        RecyclerView rec = view.findViewById(R.id.test_list);
        Context context = view.getContext();
        Button createBtn = view.findViewById(R.id.createList);
        Button deleteBtn = view.findViewById(R.id.delList);
        EditText listName_Box = view.findViewById(R.id.listName);
        EditText shopName_Box = view.findViewById(R.id.shopname);

//current list button on click
        deleteBtn.setOnClickListener(v -> {
            if(!listName_Box.getText().toString().equals("")){
                ItemList list = listsViewModel.setItemtoDel();
                if(list != null){
                    listsViewModel.removeList(list);
                    listsViewModel.setCurrentList(null);
                    Toast.makeText(context, "List has been removed", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context, "no list selected", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(context, "list name is missing", Toast.LENGTH_SHORT).show();
            }
        });

        createBtn.setOnClickListener(v -> {
            String listName = listName_Box.getText().toString();
            String shopName = shopName_Box.getText().toString();
            if(!listName.equals("")){
                listsViewModel.createList(listName,shopName);
                rec.setVisibility(View.VISIBLE);
                Toast.makeText(context, "List created,Please add ur items", Toast.LENGTH_SHORT).show();
            }
        });

//Recycler on click
        rec.setLayoutManager(new LinearLayoutManager(context));
        ListRecyclerViewAdapter adapter = new ListRecyclerViewAdapter(new ListRecyclerViewAdapter.ItemClickListener() {

            @Override
            public void onItemSaveClick(int list_Id,int item_Id, int quantity) {
                if(list_Id != -1){
                    assoViewModel.addAsso(list_Id,item_Id,quantity);
                }else{
                    Toast.makeText(context, "no list selected", Toast.LENGTH_SHORT).show();
                }

            }
            @Override
            public void onItemRemoveClick(Item item) {
            }
        });
        rec.setAdapter(adapter);

//observe methods
        itemViewModel.getLiveItems().observe(requireActivity(), items -> {
            adapter.updateViewItems(items);
            adapter.notifyDataSetChanged();
        });

        assoViewModel.getLiveAssos().observe(requireActivity(),assoList ->{
            adapter.updateListItems(assoList);
            adapter.notifyDataSetChanged();
        });

        listsViewModel.getLiveList().observe(requireActivity(), itemList -> {
            if(itemList == null){
                listName_Box.setText("");
                shopName_Box.setText("");
                assoViewModel.setAsso(-1);
                rec.setVisibility(View.INVISIBLE);

            }else{
                listName_Box.setText(itemList.getListName());
                shopName_Box.setText(itemList.getShopName());
                rec.setVisibility(View.VISIBLE);
                assoViewModel.setAsso(itemList.getList_Id());
            }
        });
        listsViewModel.getLiveList().observe(requireActivity(),curList->{
            int list_Id = -1;
            if(curList !=null ){
                list_Id = curList.getList_Id();
            }
            adapter.updateListId(list_Id);
        });

        return view;
    }
}