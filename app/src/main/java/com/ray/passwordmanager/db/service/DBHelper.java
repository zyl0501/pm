package com.ray.passwordmanager.db.service;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ray.passwordmanager.db.dao.DaoMaster;
import com.ray.passwordmanager.db.dao.DaoSession;

/**
 * Created by Ray on 15/6/18.
 */
public class DBHelper {
    private static final String TAG = DBHelper.class.getSimpleName();

    private static DBHelper instance = null;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context = null;
    private int loginUserId = 0;

    public static DBHelper getInstance() {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper();
                }
            }
        }
        return instance;
    }

    private DBHelper() {
    }

    public void initDbHelp(Context ctx, int loginId) {
        if (ctx == null || loginId <= 0) {
            throw new RuntimeException("#DBHelper# init DB exception!");
        }
        // 临时处理，为了解决离线登陆db实例初始化的过程
        if (context != ctx || loginUserId != loginId) {
            context = ctx;
            loginUserId = loginId;
            close();
            Log.i(TAG, "DB init,loginId:" + loginId);
            String DBName = "pm_" + loginId + ".db";
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(ctx, DBName, null);
            this.openHelper = helper;
        }
    }

    /**
     * Query for readable DB
     */
    protected DaoSession openReadableDb() {
        isInitOk();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    /**
     * Query for writable DB
     */
    protected DaoSession openWritableDb(){
        isInitOk();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }

    private void isInitOk() {
        if (openHelper == null) {
            Log.e(TAG, "Init not success or start,cause by openHelper is null");
            // 抛出异常 todo
            throw new RuntimeException(TAG + "#isInit not success or start,cause by openHelper is null");
        }
    }

    /**
     * 上下文环境的更新
     * 1. 环境变量的clear
     * check
     */
    public void close() {
        if (openHelper != null) {
            openHelper.close();
            openHelper = null;
            context = null;
            loginUserId = 0;
        }
    }
}
