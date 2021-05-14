package com.ignas.android.groceryshoppingapp.Service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ignas.android.groceryshoppingapp.MainActivity;
import com.ignas.android.groceryshoppingapp.R;

public class Notification extends BroadcastReceiver {
    private static final String CHANNEL_ID = "0";
    NotificationCompat.Builder notification;
    public Notification(){}
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        String name = intent.getStringExtra("name");
        String time = intent.getStringExtra("time");
        int type = intent.getIntExtra("type",0);
        int item_Id = intent.getIntExtra("item_Id",0);
        if( type == 1 ){ //create shopping date notification

            Log.i("log", "shopping date ntf creation---> "+name);


            //create Brought action Button
            Intent shoppingIntent = new Intent(context, MainActivity.class);
            shoppingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, shoppingIntent, PendingIntent.FLAG_UPDATE_CURRENT);


            //create main notification
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_shopping)
                    .setAutoCancel(true)
                    .setOngoing(true)
                    .setContentTitle("Its time to do "+name+" "+time)
                    .setContentText(" ")
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .addAction(R.drawable.ic_baseline_stop_circle_24,"Lets go",pendingIntent)
                    .setSound(uri);


            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(0,notification.build());



        }else{//create normal notification

            Log.wtf("log", "creating notification for  ---> "+name);


            //create Brought action Button
            Intent brought = new Intent(context, AlarmService.class);
            Bundle bundle = new Bundle();
            bundle.putInt("item_Id",item_Id);
            brought.putExtra("item_Id",bundle);

            PendingIntent pendingIntent = PendingIntent.getService(
                    context, 0, brought, PendingIntent.FLAG_UPDATE_CURRENT);


//create body of notification
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_baseline_no_food_24)
                    .setAutoCancel(true)
                    .setOngoing(true)
                    .setContentTitle(name+" is running out at "+time)
                    .setContentText(" ")
                    .setPriority(NotificationCompat.PRIORITY_MIN)
                    .addAction(R.drawable.ic_baseline_stop_circle_24,"OK",pendingIntent)
                    .setSound(uri);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(0,notification.build());
        }


    }
    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "0";
             String description = "0";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }
}
