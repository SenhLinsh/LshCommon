package com.linsh.lshapp.common.common;

import android.app.Application;

import com.linsh.utilseverywhere.Utils;


/**
 * <pre>
 *    author : Senh Linsh
 *    date   : 2017/10/03
 *    desc   :
 * </pre>
 */
public abstract class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
        Config.setConfig(getConfig());
    }

    protected abstract Config getConfig();
}
