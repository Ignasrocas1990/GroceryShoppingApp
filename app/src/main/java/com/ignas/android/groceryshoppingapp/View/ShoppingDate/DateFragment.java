package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.View.Item.ItemViewModel;

import org.jetbrains.annotations.NonNls;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        Context context = view.getContext();
        SwitchCompat notificationSwitch = view.findViewById(R.id.notificationSwitch);
        TextView dateTextView = view.findViewById(R.id.datePicker);
        Button schedule = view.findViewById(R.id.schedule);
        DateViewModel dateViewModel = ViewModelProviders.of(requireActivity()).get(DateViewModel.class);
        ItemViewModel itemViewModel = ViewModelProviders.of(requireActivity()).get(ItemViewModel.class);

        Calendar now = Calendar.getInstance(Locale.getDefault());
        Calendar later = Calendar.getInstance(Locale.getDefault());
        final String[] displayDate = new String[1];

        final boolean[] flag = {false};//testing (deletable)------------------
        itemViewModel.getLiveShoppingDate().observe(requireActivity(), item -> {
            if(item !=null){
                dateTextView.setText(TestDate(item));//testing (deletable)---------
                //if(flag[0]) dateTextView.setText(displayDate[0]);//Testing (deletable)-------------
                //flag[0] = true;//testing (deletable)------------------

            }else{
                dateTextView.setText(R.string.NoDate);
            }
        });
        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        displayDate[0] = dayOfMonth+"/"+month+"/"+year;
                        String dateString = dateTextView.getText().toString();
                            later.set(year,month,dayOfMonth);//for testing need to select hours/min and sec
                            if(later.compareTo(now) > 0){
                               // dateTextView.setText(displayDate);
                                int lastingDays = (int) ((later.getTimeInMillis()-now.getTimeInMillis())/1000/60/60/24);// need testing-----<<<debug
                                itemViewModel.createShoppingDate(lastingDays);

                            }else{
                                Toast.makeText(context, "Error,Date selected is in the past.", Toast.LENGTH_SHORT).show();
                            }
                    }
                },now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_WEEK));
                dpDialog.setCancelable(false);
                dpDialog.setOnCancelListener(dialog -> {
                    dialog.dismiss();
                    itemViewModel.delShoppingDate();
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
                dateViewModel.setSwitch(isChecked);
            }
        });
        return view;
    }
    public String TestDate(Item item){ // test (deletable)-------------------------------
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_WEEK,item.getLastingDays());
       String text = formatter.format(calendar.getTime());
       return text;
    }
}