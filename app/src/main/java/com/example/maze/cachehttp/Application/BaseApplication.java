package com.example.maze.cachehttp.Application;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * Created by Administrator on 2016/10/17 0017.
 */

public class BaseApplication extends Application {

    private static Context appContext;
    private static Resources rs;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        rs = getResources();
        initRealm();
    }

    private void initRealm() {
        Realm.init(this);
        RealmConfiguration.Builder build = new RealmConfiguration.Builder();
//        build.directory(getFilesDir().getAbsoluteFile());
        build.name("xcp");
        build.schemaVersion(10);
        build.deleteRealmIfMigrationNeeded();
        build.migration(new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                // 启用deleteRealmIfMigrationNeeded，将不触发migration内部事件
                for (int i = (int) oldVersion; i < newVersion; i++){
                    switch (i){
                        default:
                            Log.e("TAGSS", "i : " + i);
                    }
                }
            }
        });
        RealmConfiguration configuration = build.build();
        Realm.setDefaultConfiguration(configuration);
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static Resources getRs(){
        return rs;
    }
}
