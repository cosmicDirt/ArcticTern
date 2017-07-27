package com.mirrordust.telecomlocate.app;

import android.app.Application;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by LiaoShanhe on 2017/07/06/006.
 */

public class TelecomLocate extends Application {
    public static final String TAG = "Application subclass: TelecomLocate";
    private long mTimeInterval; // in milliseconds

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

    }
}
