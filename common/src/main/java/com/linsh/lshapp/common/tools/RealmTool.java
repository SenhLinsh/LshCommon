package com.linsh.lshapp.common.tools;

import android.app.Application;
import android.util.Log;

import com.linsh.lshutils.utils.Basic.LshIOUtils;

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
        long lastBackupRealmTime = CommonSpTools.getLastBackupRealmTime();

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
            LshIOUtils.close(realm);
        }
        return false;
    }
}
