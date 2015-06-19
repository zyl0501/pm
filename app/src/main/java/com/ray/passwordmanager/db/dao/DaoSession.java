package com.ray.passwordmanager.db.dao;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.ray.passwordmanager.db.entity.PasswordEntity;

import com.ray.passwordmanager.db.dao.PasswordDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig passwordDaoConfig;

    private final PasswordDao passwordDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        passwordDaoConfig = daoConfigMap.get(PasswordDao.class).clone();
        passwordDaoConfig.initIdentityScope(type);

        passwordDao = new PasswordDao(passwordDaoConfig, this);

        registerDao(PasswordEntity.class, passwordDao);
    }
    
    public void clear() {
        passwordDaoConfig.getIdentityScope().clear();
    }

    public PasswordDao getPasswordDao() {
        return passwordDao;
    }

}
