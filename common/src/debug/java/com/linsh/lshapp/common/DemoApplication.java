package com.linsh.lshapp.common;

import com.linsh.lshapp.common.base.BaseApplication;
import com.linsh.protocol.Config;
import com.linsh.protocol.config.FileConfig;
import com.linsh.protocol.config.HttpConfig;
import com.linsh.protocol.config.ImageConfig;
import com.linsh.protocol.config.LogConfig;
import com.linsh.protocol.config.ThreadConfig;
import com.linsh.protocol.config.UIConfig;
import com.linsh.utilseverywhere.ContextUtils;

import java.io.File;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2018/12/05
 *    desc   :
 * </pre>
 */
public class DemoApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected Config getConfig() {
        return new Config.Builder()
                .http(new HttpConfig.Builder()
                        .baseUrl("http://www.github.com/")
                        .build())
                .image(new ImageConfig.Builder()
                        .build())
                .thread(new ThreadConfig())
                .file(new FileConfig.Builder()
                        .appDir(new File("sdcard/linsh/common"))
                        .build())
                .log(new LogConfig.Builder()
                        .cacheDir(ContextUtils.getCacheDir())
                        .build())
                .ui(new UIConfig.Builder()
                        .resDir(new File("sdcard/linsh/common/res"))
                        .commonResDir(new File("sdcard/linsh/common/res"))
                        .build())
                .build();
    }

}
