package com.ignas.android.groceryshoppingapp.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.ignas.android.groceryshoppingapp.Models.Item;

import java.util.ArrayList;

public class Update extends Service {

    RealmDb db;
    ArrayList<Item> list;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        RealmDb db = new RealmDb();
        list=intent.getParcelableArrayListExtra("update");
        db.update(list);

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            db.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
