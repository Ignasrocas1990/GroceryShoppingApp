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
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ignas.android.groceryshoppingapp.Logic.ItemResources;
import com.ignas.android.groceryshoppingapp.Models.Item;
import com.ignas.android.groceryshoppingapp.Service.Realm.RealmDb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;

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
            item_Id = bundle.getInt("item_Id");

        }
        // name = intent.getStringExtra("name");
        // runoutDate = intent.getLongExtra("time", 1);
        //type = intent.getIntExtra("type", 0);


//flag=1 when service from notification (reschedule notification)
            if(flag !=-1) {
                if (manager != null) {
                    manager.cancel(0);
                }

                //connect to db and smallest item
                item =  new RealmDb().getSmallestDateItem(item_Id);


                //ItemResources rec = new ItemResources();
                //Item item = rec.re_scheduleAlarm();

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
            @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy k:m:s");
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