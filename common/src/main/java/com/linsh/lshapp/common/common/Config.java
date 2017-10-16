package com.linsh.lshapp.common.common;

/**
 * <pre>
 *    author : Senh Linsh
 *    date   : 2017/10/03
 *    desc   : APP 全局的配置
 * </pre>
 */
public abstract class Config {

    private static Config sConfig;

    static void setConfig(Config config) {
        sConfig = config;
    }

    public static Config get() {
        return sConfig;
    }

    public abstract String getAppNameEn();

    public abstract String getAppNameCn();
}
