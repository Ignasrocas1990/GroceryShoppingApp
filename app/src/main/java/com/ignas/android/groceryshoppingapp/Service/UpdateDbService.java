package com.ignas.android.groceryshoppingapp.Service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.ignas.android.groceryshoppingapp.Logic.dbHelper;
import com.ignas.android.groceryshoppingapp.Models.Item;

import java.util.ArrayList;

public class UpdateDbService extends Service {


    RealmDb db;
    ArrayList<Item> list;
    Context a;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        list=intent.getParcelableArrayListExtra("update");
        dbHelper helper = dbHelper.getInstance();
        helper.setContext(this);
        helper.update(list);

        return START_NOT_STICKY;
    }
}
