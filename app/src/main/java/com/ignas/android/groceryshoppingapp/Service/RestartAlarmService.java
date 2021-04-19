package com.ignas.android.groceryshoppingapp.Service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.ignas.android.groceryshoppingapp.Logic.dbHelper;

public class RestartAlarmService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if(intent != null){
            manager.cancel(intent.getIntExtra("time", 0));
        }
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("RestartService");
        broadcastIntent.setClass(this, dbHelper.class);
        this.sendBroadcast(broadcastIntent);

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("RestartService");
        broadcastIntent.setClass(this, dbHelper.class);
        this.sendBroadcast(broadcastIntent);
    }
}
