package com.linsh.lshapp.common.base;

import android.app.Application;

import com.linsh.lshapp.common.common.LshManagerFactory;
import com.linsh.protocol.ClientIniter;
import com.linsh.protocol.Config;
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
        Config config = getConfig();
        ClientIniter.initClient(new LshManagerFactory(config), config);
    }

    protected abstract Config getConfig();
}
