package com.ignas.android.groceryshoppingapp.View.Layer.ShoppingDate;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;

import com.ignas.android.groceryshoppingapp.R;

public class ShoppingDateFragment extends Fragment {

    private static final String TAG = "log";

    public ShoppingDateFragment() {
    }

    public static ShoppingDateFragment newInstance() {
        return new ShoppingDateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_date, container, false);
        SwitchCompat notificationSwitch = view.findViewById(R.id.notificationSwitch);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        Button schedule = view.findViewById(R.id.schedule);

        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.i(TAG, "onDateChanged: "+dayOfMonth+"/"+monthOfYear+"/"+year);
            }
        });

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i(TAG, "notifications check: "+isChecked);
            }
        });

        return view;
    }
}