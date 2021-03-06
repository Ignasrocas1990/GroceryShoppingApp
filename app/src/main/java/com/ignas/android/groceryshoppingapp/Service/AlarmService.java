package com.ignas.android.groceryshoppingapp.Service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ignas.android.groceryshoppingapp.MainActivity;
import com.ignas.android.groceryshoppingapp.Models.Item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
/***
 * Author:Ignas Rocas
 * Student Id: C00135830
 * Date: 28/05/2021
 * Purpose: Project, AlarmService that runs in the background
 */
public class AlarmService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null){
            return START_STICKY;
        }

        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Item item;
        String dateTag="",name="";
        long runoutDate=1;
        int flag=1,type=0;
        int item_Id=0;

        flag = intent.getIntExtra("flag", 1);
        Bundle bundle = intent.getBundleExtra("item_Id");
        if (bundle!=null){
            item_Id = bundle.getInt("item_Id",0);
            flag = bundle.getInt("type",0);
            if(flag==2){
                Log.wtf("log", "alarm in bundle with flag : "+flag);

                Intent toMainActivity = new Intent(this,MainActivity.class);
                toMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                toMainActivity.putExtra("type",1);
                try {
                    startActivity(toMainActivity);
                }catch(Exception e){
                    Log.i("log", "start act: erro"+e.getMessage());
                }

            }
        }
//flag=1 when service from notification (reschedule notification)
           if(flag !=-1) {
                if (manager != null) {
                    manager.cancel(0);
                }

                //connect to db and smallest item
                item =   Repository.getInstance().getSmallestDateItem(item_Id);

                if (item != null) {
                    item_Id=item.getItem_id();
                    if (item.getItem_id() == Integer.MAX_VALUE) {//check if its a shopping item
                        type = 1;
                    }
                    name = item.getItemName();
                    runoutDate = item.getRunOutDate().getTime();
                } else {
                    name = "";
                    Log.wtf("log", "all notifications received ");
                }
            }

// if has a name create alarm -else- cancel alarm (from activity)
        if(!name.equals("")){
            //@SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");//TODO change to above for normal use
            @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy k:m:s");//TODO change to above for normal use
            dateTag = formatter.format(runoutDate);


            //create new alarm notification
            Intent newIntent = new Intent(this, Notification.class);
            newIntent.putExtra("name",name);
            newIntent.putExtra("time",dateTag);
            newIntent.putExtra("type",type);
            newIntent.putExtra("item_Id",item_Id);


            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    this, 0, newIntent, PendingIntent.FLAG_CANCEL_CURRENT);



            AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            if( alarmManager != null) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, runoutDate, pendingIntent);
            }

            Log.i("log", "alarm :"+dateTag+" for item ::: "+name);

            return START_STICKY;

        }else{

            stopAlarm(this);

            if(intent != null && manager!=null){
                manager.cancel(0);
            }

            return START_NOT_STICKY;
        }

    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //stop any alarms
    public void stopAlarm(Context context){

        Intent intent  = new Intent(context, Notification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_NO_CREATE);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        if(pendingIntent != null && alarmManager!=null) {
            alarmManager.cancel(pendingIntent);
        }
        if(pendingIntent==null){ Log.i("log", "stopAlarm: success with pendingIntent"); }
        onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}