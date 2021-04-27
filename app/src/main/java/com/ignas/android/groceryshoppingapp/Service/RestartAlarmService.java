package com.ignas.android.groceryshoppingapp.Service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.ignas.android.groceryshoppingapp.Logic.ItemResources;

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
            assert manager != null;
            manager.cancel(0);
        }
        Intent broadcastIntent = new Intent();
        broadcastIntent.setClass(this, ItemResources.class);
        this.sendBroadcast(broadcastIntent);
        Log.i("log", "restart of alarm ---------> ");
        return START_STICKY;
    }
}
