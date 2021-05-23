package com.ignas.android.groceryshoppingapp.View.Item;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ignas.android.groceryshoppingapp.Logic.ItemViewModel;
import com.ignas.android.groceryshoppingapp.Models.Association;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Models.ItemList;
import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.Logic.AssoViewModel;
import com.ignas.android.groceryshoppingapp.Logic.ListsViewModel;

import java.util.List;

public class ItemFragment extends Fragment {
    public final String TAG = "log";
    private static final int MAX_PRICE_LENGTH  = 7;
    private static final int MAX_CHARS = 20,MAX_TIME_LENGTH=5;
    private Context context;

    public ItemFragment() {}

    public static ItemFragment newInstance() {
        return new ItemFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    EditText product_name,lasting_days,price;
    FloatingActionButton saveBtn,deleteBtn,syncBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.item_recycler, container, false);
        context = view.getContext();
        AssoViewModel assoViewModel = ViewModelProviders.of(requireActivity()).get(AssoViewModel.class);
        ItemViewModel itemViewModel = ViewModelProviders.of(requireActivity()).get(ItemViewModel.class);
        ListsViewModel listsViewModel = ViewModelProviders.of(requireActivity()).get(ListsViewModel.class);
        final int[] curPosition = new int[1];
        curPosition[0]=-1;

        saveBtn=view.findViewById(R.id.saveFab);
        deleteBtn=view.findViewById(R.id.delFab);
        syncBtn = view.findViewById(R.id.reSync);

        product_name = view.findViewById(R.id.cur_productName);
        lasting_days = view.findViewById(R.id.cur_lastingDays);
        price = view.findViewById(R.id.cur_price);


// Set the RecyclerView adapter
        RecyclerView recyclerView = view.findViewById(R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

        ItemRecyclerAdapter adapter = new ItemRecyclerAdapter(new ItemRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, String newName, String newDays, String newPrice) {
                product_name.setText(newName);
                price.setText(newPrice);
                lasting_days.setText(newDays);
                curPosition[0] = position;
            }
        });
        recyclerView.setAdapter(adapter);
// add item
        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String stringName = product_name.getText().toString();
                String stringDays = lasting_days.getText().toString();
                String stringPrice = price.getText().toString();

                if(ApproveData(stringName,stringDays,stringPrice)) {
                    if (curPosition[0] == -1){
                        //add item(clear item view)
                        itemViewModel.createItem(stringName,stringDays,stringPrice);
                        curPosition[0] = -1;
                        product_name.setText("");
                        lasting_days.setText("");
                        price.setText("");
                        Toast.makeText(context, "Item is created", Toast.LENGTH_SHORT).show();
                        adapter.notifyItemInserted(adapter.getItemCount()-1);
                    }else{
                        //change item info (clear item view)
                        itemViewModel.changeItem(curPosition[0],stringName,stringDays,stringPrice);
                        curPosition[0] = -1;
                        product_name.setText("");
                        lasting_days.setText("");
                        price.setText("");
                        Toast.makeText(context, "Item is changed", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                List<ItemList> foundList = null;
                String messageString="";
                if(curPosition[0] != -1){
    //find item to be deleted & assoc's for that item
                    Item deleteItem = itemViewModel.findItem(curPosition[0]);
                    final List<Association> foundAssos = assoViewModel.apartOfList(deleteItem);

     //find lists that that item is part of
                    if(foundAssos.size()!=0){
                        foundList = listsViewModel.findLists_forItem(foundAssos);
                        if(foundList.size()!=0){
      //compile error message
                            for(ItemList list: foundList){
                                messageString+=list.getListName()+" ";
                            }
                        }
                    }
                    if(!messageString.equals("")){
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        List<ItemList> foundLists = foundList;
                        builder.setCancelable(false)
                                .setIcon(R.drawable.ico_about_to_delete)
                                .setTitle("Item deletion Notice")
                                .setMessage("Item is part of these lists : "+messageString)
                                .setNegativeButton("CANCEL", (dialog, which) -> { dialog.dismiss(); })
                                .setPositiveButton("OK",(dialog,which)->{

             //deletion confirmed -- remove any item associations to lists & actual item
                                    assoViewModel.removeItemsAssos(foundLists,deleteItem);
                                    itemViewModel.removeItem(curPosition[0]);
                                    adapter.notifyItemRemoved(curPosition[0]);
                                    adapter.notifyItemRangeChanged(curPosition[0],adapter.getItemCount()-1);

                                    curPosition[0] = -1;
                                    product_name.setText("");
                                    lasting_days.setText("");
                                    price.setText("");

                                    Toast.makeText(context, "Item has been deleted", Toast.LENGTH_SHORT).show();
                                }).show();
                    }else{
                        itemViewModel.removeItem(curPosition[0]);
                        adapter.notifyItemRemoved(curPosition[0]);
                        adapter.notifyItemRangeChanged(curPosition[0],adapter.getItemCount()-1);

                        curPosition[0] = -1;
                        product_name.setText("");
                        lasting_days.setText("");
                        price.setText("");

                        Toast.makeText(context, "Item has been deleted", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "No item selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
//sync button
        syncBtn.setOnClickListener(view1 -> {

            if(curPosition[0] != -1){
                Item foundItem = itemViewModel.reSyncCurrent(curPosition[0]);
                assoViewModel.markAsBought(foundItem.getItem_id(),"0");

                Toast.makeText(context, "Item has been updated", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "No item selected", Toast.LENGTH_SHORT).show();
            }

        });
//updates live items
            itemViewModel.getLiveItems().observe(requireActivity(), items -> adapter.updateViewItems(items));

        return view;
    }
//method to approve user input
    private boolean ApproveData(String stringName, String stringDays, String stringPrice) {
        if(stringName.equals("")) {
            Toast.makeText(context, "Name field is empty", Toast.LENGTH_SHORT).show();
        }else if(stringName.length() >= MAX_CHARS){
            Toast.makeText(context,"Name is above 20 Character's",Toast.LENGTH_SHORT).show();
        }else if(Integer.parseInt(stringDays) < 0){
            Toast.makeText(context,"Lasting days cant be negative",Toast.LENGTH_SHORT).show();
        }else if(stringDays.length() >= MAX_TIME_LENGTH ){
            Toast.makeText(context, "Item can't last that long (max 5 digits)", Toast.LENGTH_SHORT).show();
        }else if(Float.compare(Float.parseFloat(stringPrice),0.f) < 0){
            Toast.makeText(context, "Quantity cant be negative", Toast.LENGTH_SHORT).show();
        }else if(stringPrice.length() >= MAX_PRICE_LENGTH){
            Toast.makeText(context, "Price cant be above 7 digits", Toast.LENGTH_SHORT).show();
        }else{
            return true;
        }
        return false;
    }
}