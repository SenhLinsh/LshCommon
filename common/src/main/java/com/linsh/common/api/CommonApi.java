package com.linsh.common.api;

import io.reactivex.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Senh Linsh on 17/6/12.
 */

public interface CommonApi {

    @GET()
    String get(@Url String url);

    @GET()
    Flowable<String> getFlowable(@Url String url);

    @GET
    ResponseBody download(@Url String url);
}
