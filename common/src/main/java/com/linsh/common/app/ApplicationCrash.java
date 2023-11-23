package com.linsh.common.app;

import com.linsh.base.BuildConfig;
import com.linsh.base.LshLog;
import com.linsh.utilseverywhere.AppUtils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2021/02/03
 *    desc   : 应用崩溃状况处理
 * </pre>
 */
class ApplicationCrash {

    public static void register() {
        // 抓取崩溃信息进行打印
        final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
            // 打印崩溃日志
            LshLog.fatal(BuildConfig.TAG, "Application crashed, thread: " + t.getName(), e);
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(t, e);
            } else {
                AppUtils.killCurrentProcess();
            }
        });
    }
}
