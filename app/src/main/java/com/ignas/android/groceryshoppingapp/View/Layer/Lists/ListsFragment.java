package com.ignas.android.groceryshoppingapp.View.Layer.Lists;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
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

import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.R;
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
        AssoViewModel assoViewModel = ViewModelProviders.of(requireActivity()).get(AssoViewModel.class);


        RecyclerView rec = view.findViewById(R.id.test_list);
        Context context = view.getContext();
        Button createBtn = view.findViewById(R.id.createList);
        Button deleteBtn = view.findViewById(R.id.delList);
        EditText listName_Box = view.findViewById(R.id.listName);
        EditText shopName_Box = view.findViewById(R.id.shopname);

//current list button on click
        deleteBtn.setOnClickListener(v -> {
            if(!listName_Box.getText().toString().equals("")){
                ItemList list = listsViewModel.setCurrentToDel();
                if(list != null){                                 //show user Dialog Box

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setCancelable(false)
                            .setIcon(R.drawable.ico_about_to_delete)
                            .setTitle("Alert, List Deletion")
                            .setMessage("Your about to delete a list, please select OK to confirm")
                            .setNegativeButton("Cancel", (dialog, which) -> { dialog.dismiss(); })
                            .setPositiveButton("OK", (dialog, which) -> {

                                assoViewModel.removeListAssos(list);
                                listsViewModel.removeList(list);
                                listsViewModel.setCurrentList(null);
                                Toast.makeText(context, "List has been removed", Toast.LENGTH_SHORT).show();
                            }).show();
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
                if(listsViewModel.getConvertedList() == null){
                    listsViewModel.createList(listName,shopName);
                    rec.setVisibility(View.VISIBLE);
                    Toast.makeText(context, "List created,Please add ur items", Toast.LENGTH_SHORT).show();
                }else{
                    listsViewModel.modifyList(listName,shopName);
                    Toast.makeText(context, "List details has been modified", Toast.LENGTH_SHORT).show();
                }


            }else{
                Toast.makeText(context, "Please specify list name", Toast.LENGTH_SHORT).show();
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
            public void onItemRemoveClick(int item_Id) {
                if(item_Id!=-1){
                    assoViewModel.deleteAsso(item_Id);
                }else{
                    Toast.makeText(context, "Please specify quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });
        rec.setAdapter(adapter);

//observe methods
        itemViewModel.getLiveItems().observe(requireActivity(), items -> {
            adapter.updateViewItems(items);
            adapter.notifyDataSetChanged();
        });

        listsViewModel.getCurrLiveList().observe(requireActivity(), itemList -> {
            int list_Id = -1;
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
                list_Id = itemList.getList_Id();
                Log.d("log", "selected list name : "+itemList.getListName());
            }
            adapter.updateListItems(assoViewModel.getCurrentAsso(),list_Id);
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}