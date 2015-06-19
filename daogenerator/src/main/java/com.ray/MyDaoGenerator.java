package com.ray;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by Ray on 15/6/18.
 */
public class MyDaoGenerator {
    private static String entityPath = "com.ray.passwordmanager.db.entity";

    public static void main(String args[]) throws Exception {
        int dbVersion = 2;
        Schema schema = new Schema(dbVersion, "com.ray.passwordmanager.db.dao");
        addPassword(schema);

        String path = args[0];
        new DaoGenerator().generateAll(schema, path);
    }

    private static void addPassword(Schema schema){
        Entity password = schema.addEntity("PasswordEntity");
        password.setTableName("Password");
        password.setClassNameDao("PasswordDao");
        password.setJavaPackage(entityPath);

        password.addIdProperty().autoincrement();
        password.addStringProperty("username").notNull().index();
        password.addStringProperty("password").notNull().index();
        password.addStringProperty("note").index();

        password.setHasKeepSections(true);
    }
}
