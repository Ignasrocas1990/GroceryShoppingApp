package com.ignas.android.groceryshoppingapp.View.Report;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.ignas.android.groceryshoppingapp.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReportRecyclerViewAdapter extends RecyclerView.Adapter<ReportRecyclerViewAdapter.ViewHolder> {

   private List<String> dValues;
   //private List<Item> iValues = new ArrayList<>();
   private final DateClickListener dateClickListener;
   View prevSelected = null;
   private String TAG = "log";

    public ReportRecyclerViewAdapter(ArrayList<String> dummyList, DateClickListener dateClickListener) {
        dValues = dummyList;
        this.dateClickListener = dateClickListener;
    }
    public interface DateClickListener{
        public ArrayList<String> getItems(String s);
    }


    @NotNull
    @Override
    public ReportRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_item, parent, false);
        return new ReportRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.dateTextView.setText(dValues.get(position));
    }

    @Override
    public int getItemCount() {
        return dValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView dateTextView;
        ImageButton reportImageItem;

        public ViewHolder(@NonNull @NotNull View view) {
            super(view);
            dateTextView = view.findViewById(R.id.reportDateView);
            reportImageItem = view.findViewById(R.id.reportImageItem);
            reportImageItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Log.i(TAG, "onClick: click");
            ArrayList<String> list = dateClickListener.getItems(dateTextView.getText().toString());
            PopupMenu popupMenu = new PopupMenu(view.getContext(),view);
            for(String i : list){
                popupMenu.getMenu().add(Menu.NONE,0,Menu.FLAG_PERFORM_NO_CLOSE,i);
            }
            popupMenu.show();

        }
    }
}