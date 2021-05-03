package com.ignas.android.groceryshoppingapp.View.Layer.ShoppingDate;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.ignas.android.groceryshoppingapp.R;

import org.jetbrains.annotations.NonNls;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
        Context context = view.getContext();
        SwitchCompat notificationSwitch = view.findViewById(R.id.notificationSwitch);
        TextView datePicker = view.findViewById(R.id.datePicker);
        Button schedule = view.findViewById(R.id.schedule);

        Calendar cal = Calendar.getInstance(Locale.getDefault());

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        @NonNls String displayDate = dayOfMonth+"/"+month+"/"+year;
                        datePicker.setText(displayDate);
                        cal.set(year,month,dayOfMonth);
                        dateViewModel.setShoppingDate(cal.getTime());
                    }
                },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_WEEK));
                dpDialog.setCancelable(false);
                dpDialog.setOnCancelListener(dialog -> {
                    datePicker.setText("no date has been selected");
                });
                dpDialog.show();
            }
        });

        //notificationSwitch.set
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //notificationSwitch.toggle();
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