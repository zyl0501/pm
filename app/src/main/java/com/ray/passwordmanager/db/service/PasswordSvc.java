package com.ray.passwordmanager.db.service;

import com.ray.passwordmanager.db.dao.PasswordDao;
import com.ray.passwordmanager.db.entity.PasswordEntity;

import java.util.List;

/**
 * password 本地数据库相关的逻辑处理，如增删关联表的数据等
 * Created by Ray on 15/6/18.
 */
public class PasswordSvc {

    public static void deleteAll(){
        getDao().deleteAll();
    }

    public static long insertPwd(PasswordEntity pwd){
        return getDao().insert(pwd);
    }

    public static List<PasswordEntity> getAllPassword(){
        return getDao().loadAll();
    }

    private static PasswordDao getDao(){
        return DBHelper.getInstance().openReadableDb().getPasswordDao();
    }
}
