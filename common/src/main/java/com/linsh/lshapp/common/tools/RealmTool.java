package com.linsh.lshapp.common.tools;

import android.app.Application;
import android.util.Log;

import com.linsh.utilseverywhere.IOUtils;
import com.linsh.utilseverywhere.SharedPreferenceUtils;

import java.io.File;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * <pre>
 *    author : Senh Linsh
 *    date   : 17/10/3
 *    desc   : Realm 数据库相关辅助工具
 * </pre>
 */
public class RealmTool {

    private static final String LAST_BACKUP_REALM_TIME = "last_backup_realm_time";

    public static void init(Application application, String name, int version, RealmMigration magration) {
        Realm.init(application);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(name)
                .schemaVersion(version)
                .migration(magration)
                .build();
        // 设置默认配置
        Realm.setDefaultConfiguration(config);
    }

    // 检查 Realm 数据库是否需要备份
    public static boolean checkBackupRealm() {
        long lastBackupRealmTime = getLastBackupRealmTime();

        Realm realm = Realm.getDefaultInstance();
        File file = new File(realm.getPath());
        try {
            if (file.exists()) {
                long lastModified = file.lastModified();
                return lastModified > lastBackupRealmTime;
            } else {
                Log.i("LshLog", "file not exists");
            }
        } finally {
            IOUtils.close(realm);
        }
        return false;
    }

    public static long getLastBackupRealmTime() {
        return SharedPreferenceUtils.getLong(LAST_BACKUP_REALM_TIME, 0);
    }

    public static void refreshLastBackupRealmTime() {
        SharedPreferenceUtils.putLong(LAST_BACKUP_REALM_TIME, System.currentTimeMillis());
    }
}
