package com.ray.passwordmanager.manager;

import com.ray.passwordmanager.db.entity.PasswordEntity;
import com.ray.passwordmanager.db.service.PasswordSvc;

/**
 * password 业务逻辑处理，包括本地数据，数据同步，以及UI通知等
 * Created by Ray on 15/7/3.
 */
public class PasswordManager {
    public static void addPassword(PasswordEntity entity){
        PasswordSvc.insertPwd(entity);
    }
}
