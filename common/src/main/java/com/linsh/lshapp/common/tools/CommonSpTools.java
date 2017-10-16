package com.linsh.lshapp.common.tools;

import com.linsh.lshutils.utils.Basic.LshSharedPreferenceUtils;

/**
 * Created by Senh Linsh on 17/6/14.
 */

public class CommonSpTools {

    // 上次备份 Realm 的时间
    private static final String LAST_BACKUP_REALM_TIME = "last_backup_realm_time";

    public static long getLastBackupRealmTime() {
        return LshSharedPreferenceUtils.getLong(LAST_BACKUP_REALM_TIME, 0);
    }

    public static void putLastBackupRealmTime(long time) {
        LshSharedPreferenceUtils.putLong(LAST_BACKUP_REALM_TIME, time);
    }

    public static void refreshLastBackupRealmTime() {
        LshSharedPreferenceUtils.putLong(LAST_BACKUP_REALM_TIME, System.currentTimeMillis());
    }
}
