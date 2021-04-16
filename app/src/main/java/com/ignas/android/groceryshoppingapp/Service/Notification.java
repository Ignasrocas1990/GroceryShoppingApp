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

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.ignas.android.groceryshoppingapp.Logic.Alarm;
import com.ignas.android.groceryshoppingapp.Logic.dbHelper;
import com.ignas.android.groceryshoppingapp.R;

public class Notification extends BroadcastReceiver {
    private static final String CHANNEL_ID = "0";
    NotificationCompat.Builder notification;

    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);

        String itemName = intent.getStringExtra("itemName");
        float time =intent.getFloatExtra("time",1);



        //create ok action Button
        Intent brought = new Intent(context, dbHelper.class);
        brought.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, brought, 0);

        //Create main notification

        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_no_food_24)
                .setAutoCancel(true)
                .setOngoing(true)
                .setContentTitle(itemName+" Is running out in "+ time +" hours")
                .setContentText("")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(R.drawable.ic_ok,"ok",pendingIntent)
                .setSound(uri);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0,notification.build());

    }
    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "0";
            // String description = "0";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            //channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
