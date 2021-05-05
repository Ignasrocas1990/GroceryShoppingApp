package com.ignas.android.groceryshoppingapp.View.ShoppingDate;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.Service.AlarmService;

public class ShoppingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        cancelAlarm();

    }
    public void cancelAlarm(){
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra("name","");
        intent.putExtra("flag",-1);
        startService(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = new Intent(this, AlarmService.class);
        intent.putExtra("name","restart");
        intent.putExtra("flag",1);
        startService(intent);
    }
}