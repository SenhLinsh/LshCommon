package com.linsh.lshapp.common.tools;

import com.linsh.lshapp.common.api.CommonApi;


/**
 * <pre>
 *    author : Senh Linsh
 *    date   : 2017/10/03
 *    desc   : 基础 API 生成器
 * </pre>
 */
public class CommonApiCreator {

    public static final String BASE_URL_GITHUB = "https://github.com/SenhLinsh/";

    public static CommonApi getCommonApi() {
        return RetrofitHelper.createStringApi(CommonApi.class, BASE_URL_GITHUB);
    }
}
