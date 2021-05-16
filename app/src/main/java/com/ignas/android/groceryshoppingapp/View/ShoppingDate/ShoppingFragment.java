package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import android.content.Context;
import android.os.Bundle;
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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.View.Item.ItemViewModel;
import com.ignas.android.groceryshoppingapp.View.Lists.AssoViewModel;
import com.ignas.android.groceryshoppingapp.View.Lists.ListsViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class ShoppingFragment extends Fragment{
    private static final int MAX_LENGTH  = 8;
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
        ArrayAdapter[] spinnerAdapter = new ArrayAdapter[1];
        int[] currSpinnerPos = {-1};

        //query data for spinner and recycler
        ArrayList<Item> notifiedItems = itemViewModel.getNotifiedItems();
        ArrayList<ItemList> lists = new ArrayList<ItemList>();
        lists.addAll(Objects.requireNonNull(listViewModel.getLiveLists().getValue()));

        HashMap<Integer,ArrayList<Association>> shoppingAssos = assoViewModel.findShoppingAssos(lists,notifiedItems);
        lists = listViewModel.removeEmptyLists(lists,shoppingAssos);


        // ArrayList<Item> items = itemViewModel.createShoppingItems();
            //ArrayList<Association> displayAssos = assoViewModel.findAssociations(items);
           // ArrayList<ItemList> lists =  listViewModel.findLists_forItem(displayAssos);

            // ArrayList<Item> itemWithoutList = dateViewModel.createItems(items, displayAssos, lists);
            // assoViewModel.createAssos(itemWithoutList);

//set spinner
        spinnerAdapter[0] = new ArrayAdapter<>(context
                , android.R.layout.simple_spinner_item, lists);
        spinnerAdapter[0].setNotifyOnChange(true);
        spinnerAdapter[0].setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listSpinner.setAdapter(spinnerAdapter[0]);


//setts off when
        //--------- RECYCLING View

        ArrayList<ItemList> finalLists = lists;
        ShoppingRecyclerAdapter recyclerAdapter = new ShoppingRecyclerAdapter(notifiedItems,new ShoppingRecyclerAdapter.onItemClickListener() {
            @Override
            public void onItemBuy(Item item, Association currentAsso, boolean deleteFlag) {
                if(totalView.getText().toString().equals("total")){
                    dateViewModel.setTotal(item.getPrice());
                }else{
                    dateViewModel.addToTotal(item.getPrice());
                }
                assoViewModel.onBuyBought(currentAsso,shoppingAssos);
                itemViewModel.syncBoughtItem(item);

                listViewModel.removeEmptyLists(finalLists,shoppingAssos);
                if(deleteFlag) {
                    listSpinner.setSelection(0, true);
                }
            }
        });
        recyclerView.setAdapter(recyclerAdapter);


// attaching data rcyclerAdapter to spinner and setItemSelected
        listSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position !=0) {
                    currSpinnerPos[0] = position;
                    ItemList selectedList = new ItemList();
                    selectedList = (ItemList) parent.getItemAtPosition(position);
                    recyclerAdapter.setItems(shoppingAssos.get(selectedList.getList_Id()));
                    recyclerAdapter.notifyDataSetChanged();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {
                currSpinnerPos[0]=-1;
            }
        });
//adding item on a fly
        ArrayList<ItemList> finalLists1 = lists;
        addBtn.setOnClickListener(new View.OnClickListener() {
            Association newAsso=null;
            @Override
            public void onClick(View v) {
//get & approves new item data
                String nameString = nameEditText.getText().toString();
                String amountString =  amountEditText.getText().toString();
                String priceString = priceEditText.getText().toString();
                if(ApproveNewData(nameString,amountString,priceString)){
                   Item newItem =  itemViewModel.createItem(nameString, "0", priceString);
                   newItem.setNotified(true);
                   notifiedItems.add(newItem);
                   newAsso =  assoViewModel.createTempAsso(newItem.getItem_id(),amountString);

                   nameEditText.setText("");
                   amountEditText.setText("");
                   priceEditText.setText("");

                    Toast.makeText(context, "On a fly item added", Toast.LENGTH_SHORT).show();
//check if there is no other lists shown
                if(finalLists.get(0).getToStringText().equals("no items to buy")){
                    finalLists.get(0).setToStringText("select a list");
                        ItemList newItemList = new ItemList();
                        newItemList.setList_Id(0);
                        newItemList.setToStringText("on a fly");
                        finalLists1.add(1,newItemList);
                        ArrayList<Association> tempList = new ArrayList<>();
                        tempList.add(newAsso);
                        shoppingAssos.put(0,tempList);

                }else{//iterate through lists and add new association.
                    assoViewModel.insertOnFlyItemAsso(newAsso,shoppingAssos);
                }
              }
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