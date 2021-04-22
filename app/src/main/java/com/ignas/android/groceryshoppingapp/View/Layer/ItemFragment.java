package com.ignas.android.groceryshoppingapp.View.Layer;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.item_recycler, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            ViewModel viewModel = ViewModelProviders.of(requireActivity()).get(ViewModel.class);
            ItemRecyclerViewAdapter adapter = new ItemRecyclerViewAdapter(new ItemRecyclerViewAdapter.ItemClickListener() {
                @Override
                public void onItemSaveClick(Item item) {
                    viewModel.addItem(item);

                }
                @Override
                public void onItemRemoveClick(Item item) {
                    viewModel.removeItem(item);
                }

                @Override
                public void onItemChangeClick(int position, String newName, String newDays, String newQuantity, String newPrice) {
                    viewModel.changeItem(position,newName,newDays,newQuantity,newPrice);
                }
            });

            recyclerView.setAdapter(adapter);
            viewModel.getLiveItems().observe(requireActivity(), new Observer<ArrayList<Item>>() {
                @Override
                public void onChanged(ArrayList<Item> items) {
                    adapter.updateViewItems(items);
                    Log.i(TAG, "onChanged: message");
                }
            });
        }
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