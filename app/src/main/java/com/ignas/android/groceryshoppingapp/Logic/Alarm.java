package com.ignas.android.groceryshoppingapp.Logic;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.RemoteInput;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Alarm extends BroadcastReceiver {
    private long milliseconds = 0;
    private static final String TAG = "log";
    public Alarm() {}
    //setter & getters
    public Alarm(long milliseconds) {
        this.milliseconds = milliseconds;
    }
    public long getMilliseconds() {
        return milliseconds;
    }
    public void setMilliseconds(Date date) {
        this.milliseconds = convertToMili(date);
    }


    private int getMessage(Intent intent) {
        try {
            Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
            return Integer.parseInt(Objects.requireNonNull(remoteInput.getCharSequence("key")).toString());
        }catch (Exception e){
            return 0;
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //int hours = getMessage(intent);
/*
        if (hours == 0) {
            this.milliseconds = 10 * 1000;//10 sec default
        } else {
            this.milliseconds = 10 * 1000;
        }

 */
        setAlarm(context, intent,intent.getStringExtra("itemName"));
    }


    public void setAlarm(Context context,Intent intent,String name){

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(intent != null){
            manager.cancel(0);
        }
        //create new alarm notification
        intent = new Intent(context, Notification.class);
        intent.putExtra("itemName",name);
        float hours = ((milliseconds/1000.f)/60.f)/60.f;
        intent.putExtra("time",hours);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if( alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + milliseconds, pendingIntent);
        }
    }
    private long convertToMili(Date later) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar now = Calendar.getInstance();
        String d = formatter.format(now.getTime());
        Log.d(TAG, "getModifiedDate: "+d);
        //----TODO-------------need to convert Date to calendar
        long mills = now.getTimeInMillis()-(Calendar.getInstance().getTimeInMillis());

        return mills;
    }
    public long getModifiedDate(int days){
        //DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Calendar newCalendar = Calendar.getInstance();

        newCalendar.add(Calendar.DAY_OF_WEEK,days);
        String d = formatter.format(newCalendar.getTime());
        long mills = newCalendar.getTimeInMillis()-(Calendar.getInstance().getTimeInMillis());

        Log.d(TAG, "getModifiedDate: "+d);
        Log.d(TAG, "getModifiedSeconds: "+String.valueOf(mills));

        //String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        //cal.add(Calendar.DATE,days);
        return mills;

    }


}
