package com.ignas.android.groceryshoppingapp;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.RemoteInput;

import com.ignas.android.groceryshoppingapp.Logic.dbHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Alarm extends BroadcastReceiver {
    private long milliseconds = 0;
    private static final String TAG = "log";

    public Alarm() {}
    public Alarm(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(int days) {
        this.milliseconds = getModifiedDate(days);
    }
/*
    private int getMessage(Intent intent) {
        try {
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            return Integer.parseInt(Objects.requireNonNull(remoteInput.getCharSequence("key")).toString());
        }catch (Exception e){
            return 0;
        }

    }

 */
    @Override
    public void onReceive(Context context, Intent intent) {
/*
        int hours = getMessage(intent);

        if(hours == 0){
            this.milliseconds = 10*1000;//10 sec default
        }else{
            this.milliseconds = 10*1000;
        }
        */

        //need to create service from here-----TODO------------------
        //Intent toDbHelper = new Intent(context,dbHelper.class);
        //context.sendBroadcast(toDbHelper);



        //close notification bar

        /*
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(2021,02,26,11,24,0);//NOTE Java month index starts at 0(JAN),(DEC is 11)
        long mills = 0;
        mills = calendar.getTimeInMillis();

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){

            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, mills, pendingIntent);
        }
        else{

            alarmManager.set(AlarmManager.RTC_WAKEUP, mills, pendingIntent);
        }

        //Toast.makeText(this,"Alarm set in Seconds",Toast.LENGTH_LONG).show();


        */
    }
    public long getModifiedDate(int days){
        //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Calendar newCalendar = Calendar.getInstance();

        newCalendar.add(newCalendar.DAY_OF_WEEK,days);
        String d = formatter.format(newCalendar.getTime());
        long mills = newCalendar.getTimeInMillis()-(Calendar.getInstance().getTimeInMillis());

        Log.d(TAG, "getModifiedDate: "+d);
        Log.d(TAG, "getModifiedSeconds: "+String.valueOf(mills));

        //String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        //cal.add(Calendar.DATE,days);
        return days*1000;

    }
    public void hideNotification(Context context,Intent intent,String name){

    }
    public void setAlarm(Context context,Intent intent,String name){


        //create new alarm notification
        Intent newIntent = new Intent(context,Notification.class);
        newIntent.putExtra("name",name);
        newIntent.putExtra("time",milliseconds);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if(intent != null){
            manager.cancel(intent.getIntExtra("time", 0));
        }


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, newIntent, 0);



        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if( alarmManager != null) {
            Log.d(TAG, "setAlarm: alarm set");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    SystemClock.elapsedRealtime() + milliseconds, pendingIntent);
        }
    }
}