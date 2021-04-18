package com.ignas.android.groceryshoppingapp;

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
import androidx.core.app.RemoteInput;

import com.ignas.android.groceryshoppingapp.Logic.dbHelper;
import com.ignas.android.groceryshoppingapp.R;
import com.ignas.android.groceryshoppingapp.Service.RestartAlarmService;

public class Notification extends BroadcastReceiver {
    private static final String CHANNEL_ID = "0";
    NotificationCompat.Builder notification;
    private static final String KEY_TEXT_REPLY = "key";
    @Override
    public void onReceive(Context context, Intent intent) {
        createNotificationChannel(context);
        String name = intent.getStringExtra("name");
        long time = intent.getLongExtra("time",1);

        //create Brought action Button
        Intent brought = new Intent(context, RestartAlarmService.class);
        brought.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getService(
                context, 0, brought, 0);

//-----------------------
//-----------------------
        //create main notification
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        notification = new NotificationCompat.Builder(context,CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_baseline_no_food_24)
                .setAutoCancel(true)
                .setOngoing(true)
                .setContentTitle(name+" is running out in "+time)
                .setContentText(" ")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .addAction(R.drawable.ic_baseline_stop_circle_24,"Brought",pendingIntent)
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
