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
import com.ignas.android.groceryshoppingapp.Service.RestartAlarmService;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static android.os.SystemClock.sleep;

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
    }
    public long getModifiedDate(int days){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Calendar later = Calendar.getInstance();

        //newCalendar.add(newCalendar.DAY_OF_WEEK,days);
        later.add(later.MILLISECOND,days*1000);//TODO extends by seconds // ----testing

        String d = formatter.format(later.getTime());
        long mills = later.getTimeInMillis()-(Calendar.getInstance().getTimeInMillis());

        Log.d(TAG, "getModifiedDate: "+d);
        Log.d(TAG, "getModifiedSeconds: "+String.valueOf(mills));

        //String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        //cal.add(Calendar.DATE,days);
        return mills;

    }
    public void setAlarm(Context context, String name, Date runoutDate){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar later = Calendar.getInstance();
        later.setTime(runoutDate);
        long milliseconds = later.getTimeInMillis()-(Calendar.getInstance().getTimeInMillis());
        String dateTag = formatter.format(later.getTime());

        //create new alarm notification
        Intent newIntent = new Intent(context,Notification.class);
        newIntent.putExtra("name",name);
        newIntent.putExtra("time",dateTag);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);



        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if( alarmManager != null) {
            Log.d(TAG, "setAlarm: alarm set");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                    SystemClock.elapsedRealtime() + milliseconds, pendingIntent);
        }

        for(int i=0;i< milliseconds/1000;i++){
            sleep(1000);
            Log.i("log", "second: "+i);
        }
    }
}