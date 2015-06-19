package com.ray.passwordmanager.app;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.ray.passwordmanager.db.dao.DaoMaster;
import com.ray.passwordmanager.db.dao.DaoSession;

/**
 * Created by Ray on 15/6/18.
 */
public class PMApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }
}