package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.Models.Report;
import com.ignas.android.groceryshoppingapp.Models.ShoppingItem;
import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.View.Item.ItemViewModel;
import com.ignas.android.groceryshoppingapp.View.Lists.AssoViewModel;
import com.ignas.android.groceryshoppingapp.View.Lists.ListsViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShoppingFragment extends Fragment {
    private static final int MAX_LENGTH  = 7;
    private static final int MAX_CHARS = 20;
    private static final String TAG = "log";
    Context context=null;
    ItemViewModel itemViewModel;
    ListsViewModel listViewModel;
    AssoViewModel assoViewModel;
    DateViewModel dateViewModel;
    RecyclerView recyclerView;
    ShoppingRecyclerAdapter adapter;
    Button addBtn;
    EditText nameEditText,amountEditText,priceEditText;
    TextView totalView;
    Spinner listSpinner;


    public ShoppingFragment() {
    }

    public static ShoppingFragment newInstance() {
        return new ShoppingFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_day, container, false);
        context = view.getContext();
         listViewModel = ViewModelProviders.of(requireActivity()).get(ListsViewModel.class);
         assoViewModel = ViewModelProviders.of(requireActivity()).get(AssoViewModel.class);
         itemViewModel = ViewModelProviders.of(requireActivity()).get(ItemViewModel.class);
         dateViewModel = ViewModelProviders.of(requireActivity()).get(DateViewModel.class);
         nameEditText = view.findViewById(R.id.newName);
         amountEditText = view.findViewById(R.id.newAmount);
         priceEditText = view.findViewById(R.id.newPrice);
        listSpinner = view.findViewById(R.id.listSpinner);

         addBtn = view.findViewById(R.id.addBtn);
         totalView = view.findViewById(R.id.total);
         recyclerView = view.findViewById(R.id.shopping_Recycler_View);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        //itemViewModel.setShoppingDate(); sets to null (need it at the shopping date run) or click on button
        ArrayList<Item> items = itemViewModel.createShoppingItems();
        ArrayList<Association> displayAssos = assoViewModel.findAssociations(items);
        ArrayList<ItemList> lists =  listViewModel.findLists_forItem(displayAssos);

        ArrayList<Item> itemWithoutList = dateViewModel.createItems(items, displayAssos, lists);
        assoViewModel.createAssos(itemWithoutList);


//setts off when
        //--------- RECYCLING View

        ShoppingRecyclerAdapter adapter = new ShoppingRecyclerAdapter(new ShoppingRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemBuy(ShoppingItem item) {
                if(totalView.getText().toString().equals("total")){
                    dateViewModel.setTotal(item.getPrice());
                }else{
                    dateViewModel.addToTotal(item.getPrice());
                }
            }
            @Override
            public void onCancel(ShoppingItem item) {
                if(totalView.getText().toString().equals("0.0")){
                    totalView.setText(R.string.total);
                }else{
                    dateViewModel.subtractTotal(item.getPrice());
                }
            }
        });
        recyclerView.setAdapter(adapter);


        final ArrayAdapter[] dataAdapter = new ArrayAdapter[1];

// Creating adapter for Spinner and observe it
        listViewModel.getLiveLists().observe(requireActivity(), new Observer<ArrayList<ItemList>>() {
            @Override
            public void onChanged(ArrayList<ItemList> itemLists) {

                dataAdapter[0] = new ArrayAdapter<>(context
                        , android.R.layout.simple_spinner_item, itemLists);

                dataAdapter[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
        });
// attaching data adapter to spinner and setItemSelected
        listSpinner.setAdapter(dataAdapter[0]);
        listSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    ItemList selectedList = new ItemList();
                    selectedList = (ItemList) parent.getItemAtPosition(position);
                    Log.i("log", "onItemSelected: "+selectedList.getListName());
                    listViewModel.setCurrentList(selectedList);
                }else{
                    listViewModel.setCurrentList(null);
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        dateViewModel.getLiveSpItems().observe(requireActivity(), new Observer<ArrayList<ShoppingItem>>() {
            @Override
            public void onChanged(ArrayList<ShoppingItem> shoppingItems) {
                adapter.setItems(shoppingItems);
                adapter.notifyDataSetChanged();
            }
        });
//total field observer
        dateViewModel.getLiveTotal().observe(requireActivity(), new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                totalView.setText(String.valueOf(aFloat));
            }
        });



        return view;
    }


    //check if data entered is not above limits
    private boolean ApproveNewData(String newName, String newAmount, String newPrice) {
        if(newName.equals("")){
            Toast.makeText(context, "Name field is empty", Toast.LENGTH_SHORT).show();
        }else if(newName.length() > MAX_CHARS) {
            Toast.makeText(context, "Name is above 20 Character's", Toast.LENGTH_SHORT).show();
        }else if(newAmount.length() > MAX_LENGTH){
            Toast.makeText(context, "Quantity cant be above 7 digits", Toast.LENGTH_SHORT).show();
        }else if(newPrice.length() > MAX_LENGTH){
            Toast.makeText(context, "Price cant be above 7 digits", Toast.LENGTH_SHORT).show();
        }else{
            return true;
        }
        return false;
    }
}