package com.ignas.android.groceryshoppingapp.Service;

import android.app.Application;

import com.ignas.android.groceryshoppingapp.Models.Item;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;

public class DbConnect extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        final RealmConfiguration config = new RealmConfiguration.Builder().name("groceryDb.realm")
                .schemaVersion(3).migration(new RealmMigrations())
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);
        Realm.getInstance(config);

    }
/*
    @Override
    public void onTerminate() {
        Realm.getDefaultInstance().close();
        super.onTerminate();
    }

 */

}
class RealmMigrations implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion == 2) {
            final RealmObjectSchema userSchema = schema.get("UserData");
            userSchema.addField("running", Item.class);

        }
    }
}
