package com.ignas.android.groceryshoppingapp.View.Layer.ShoppingDate;

import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;

import com.ignas.android.groceryshoppingapp.R;

public class DateFragment extends Fragment {

    private static final String TAG = "log";

    public DateFragment() {
    }

    public static DateFragment newInstance() {
        return new DateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_date, container, false);
        DateViewModel dateViewModel = ViewModelProviders.of(requireActivity()).get(DateViewModel.class);

        SwitchCompat notificationSwitch = view.findViewById(R.id.notificationSwitch);
        DatePicker datePicker = view.findViewById(R.id.datePicker);
        Button schedule = view.findViewById(R.id.schedule);

        //notificationSwitch.set
          //      setChecked(dateViewModel.getSwitch());
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //notificationSwitch.toggle();
            }
        });


        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Log.i(TAG, "onDateChanged: "+dayOfMonth+"/"+monthOfYear+"/"+year);
            }
        });


        dateViewModel.getLiveSwitch().observe(requireActivity(),state->{
            notificationSwitch.setChecked(state);
        });

        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dateViewModel.setSwitch(isChecked);//TODO------------if notifications off - on start of the app
                                                    // if they are on need to reschedule all of them.(reset all of them from start)
            }
        });





        return view;
    }
}