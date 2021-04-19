package com.ignas.android.groceryshoppingapp.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Alarm extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String name = intent.getStringExtra("name");
        Long runoutDate = intent.getLongExtra("time",1);


        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        long milliseconds = runoutDate-(Calendar.getInstance().getTimeInMillis());
        String dateTag = formatter.format(runoutDate);

        //create new alarm notification
        Intent newIntent = new Intent(this,Notification.class);
        newIntent.putExtra("name",name);
        newIntent.putExtra("time",dateTag);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);



        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if( alarmManager != null) {
            Log.i("log", "setAlarm: alarm set");
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    SystemClock.elapsedRealtime() + milliseconds, pendingIntent);
        }

            Log.i("log", "Alarm started for item "+name);
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}