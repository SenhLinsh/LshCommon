package com.linsh.common.app;

import android.app.Application;

import com.linsh.lshutils.utils.ActivityLifecycleUtilsEx;
import com.linsh.utilseverywhere.AppUtils;
import com.linsh.utilseverywhere.Utils;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/11/23
 *    desc   :
 * </pre>
 */
public class CommonApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        if (!AppUtils.isMainProcess()) {
            return;
        }
        ActivityLifecycleUtilsEx.init(this);
        // 抓取崩溃信息进行打印
        ApplicationCrash.register();
    }
}
