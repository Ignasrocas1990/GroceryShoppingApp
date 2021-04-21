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
import com.ignas.android.groceryshoppingapp.Models.ItemList;
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
    private ArrayList<Item> items;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {}
    public ItemFragment(ArrayList<Item> list){
        this.items = list;
    }

    // TODO: Customize parameter initialization
    public static ItemFragment newInstance(ArrayList<Item> list) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList("list",list);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            items = getArguments().getParcelableArrayList("list");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;

            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            ViewModel viewModel = ViewModelProviders.of(requireActivity()).get(ViewModel.class);
            MyItemRecyclerViewAdapter adapter = new MyItemRecyclerViewAdapter(new MyItemRecyclerViewAdapter.ItemClickListener() {
                @Override
                public void onItemSaveClick(Item item) {
                    viewModel.addItem(item);

                }
                @Override
                public void onItemRemoveClick(Item item) {
                    viewModel.removeItem(item);
                }

                @Override
                public void onItemChangeClick(Item item) {
                    viewModel.changeItem(item);
                }
            });

            recyclerView.setAdapter(adapter);
            viewModel.getLiveItems().observe(requireActivity(), new Observer<ArrayList<Item>>() {
                @Override
                public void onChanged(ArrayList<Item> items) {
                    adapter.updateViewItems(items);
                   // adapter.notifyDataSetChanged();
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