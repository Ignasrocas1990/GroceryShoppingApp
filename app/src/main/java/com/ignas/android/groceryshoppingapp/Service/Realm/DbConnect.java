package com.ignas.android.groceryshoppingapp.Service.Realm;

import android.app.Application;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
/***
 * Author:Ignas Rocas
 * Student Id: C00135830
 * Date: 28/05/2021
 * Purpose: Project, DB connection/migration class
 */
public class DbConnect extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        final RealmConfiguration config = new RealmConfiguration.Builder().name("DB.realm")
                .schemaVersion(1).migration(new RealmMigrations())
                .allowWritesOnUiThread(true)
                .allowQueriesOnUiThread(true)
                .build();
        Realm.setDefaultConfiguration(config);
        Realm.getInstance(config);

    }

}
class RealmMigrations implements RealmMigration {

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        final RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            final RealmObjectSchema userSchema = schema.get("UserData");

        }
    }
}
