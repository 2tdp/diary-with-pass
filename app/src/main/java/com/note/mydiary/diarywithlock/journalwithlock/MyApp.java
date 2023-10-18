package com.note.mydiary.diarywithlock.journalwithlock;

import android.app.Application;
import com.note.mydiary.diarywithlock.journalwithlock.sharepref.DataLocalManager;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        DataLocalManager.init(getApplicationContext());
        Realm.init(getApplicationContext());
        super.onCreate();

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .allowQueriesOnUiThread(true)
                .allowWritesOnUiThread(true)
                .name("app_diary_database.realm")
                .build();
        Realm.setDefaultConfiguration(configuration);
    }
}
