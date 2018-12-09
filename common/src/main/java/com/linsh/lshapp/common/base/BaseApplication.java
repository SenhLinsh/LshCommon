package com.linsh.lshapp.common.base;

import android.app.Application;

import com.linsh.lshapp.common.common.LshManagerFactory;
import com.linsh.lshapp.common.helper.view.JsonIdTextItemViewProtocol;
import com.linsh.lshapp.common.helper.view.JsonIdTitleTextItemViewProtocol;
import com.linsh.lshapp.common.helper.view.TextItemViewProtocol;
import com.linsh.lshapp.common.helper.view.TitleTextItemViewProtocol;
import com.linsh.protocol.Client;
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
        ClientIniter.initClient(new LshManagerFactory(getConfig()));

        Client.ui().view().register().registerProtocol(TitleTextItemViewProtocol.class, JsonIdTitleTextItemViewProtocol.class, true);
        Client.ui().view().register().registerProtocol(TextItemViewProtocol.class, JsonIdTextItemViewProtocol.class, true);
    }

    protected abstract Config getConfig();
}
