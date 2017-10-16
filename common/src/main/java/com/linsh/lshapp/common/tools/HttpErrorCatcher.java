package com.linsh.lshapp.common.tools;


import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by Senh Linsh on 16/11/10.
 */
public class HttpErrorCatcher {

    private static HttpErrorInfo parseError(Throwable thr) {
        HttpErrorInfo errorInfo = new HttpErrorInfo();
        if (thr == null) return errorInfo;

        if (thr instanceof HttpException) {
            errorInfo.httpError = HttpError.ERROR_REQUEST;
            try {
                ResponseBody body = ((HttpException) thr).response().errorBody();
                if (body != null) {
                    errorInfo.msg = "请求失败(" + body.string() + ")";
                }
            } catch (IOException e1) {
                e1.printStackTrace();
                errorInfo.msg = "请求失败(UNKNOWN)";
            }
        } else if (thr instanceof UnknownHostException) { //// 1.没有网络 2.地址不正确
            errorInfo.httpError = HttpError.ERROR_NO_NETWORK;
            errorInfo.msg = "加载失败，请确保网络连接";
        } else if (thr instanceof ConnectException) { //// 1.网址出错 2.系统休眠后屏蔽网络
            if (thr.getMessage() != null && thr.getMessage().contains("Failed to connect")) {
                errorInfo.httpError = HttpError.ERROR_URL;
                errorInfo.msg = "无法连接网络, 请稍候重试";
            }
        } else if (thr instanceof SocketTimeoutException) { //// 网络延迟，请求超时
            errorInfo.httpError = HttpError.ERROR_TIME_OUT;
            errorInfo.msg = "网络加载慢，请确保网络通畅";
        } else if (thr instanceof JsonSyntaxException) {
            errorInfo.httpError = HttpError.ERROR_PARSE_JSON;
            errorInfo.msg = "数据解析错误，开发人员正在挨板子";
        }
        if (errorInfo.httpError == null) {
            errorInfo.httpError = HttpError.ERROR_UNKNOWN;
            errorInfo.msg = "网络异常(" + thr.getMessage() + ")，请尝试重新加载";
        }
        return errorInfo;
    }

    public static String dispatchError(Throwable thr) {
        HttpErrorInfo errorInfo = parseError(thr);
        return errorInfo.msg;
    }

    public static boolean isHttpError(Throwable thr) {
        return thr instanceof HttpException
                || thr instanceof UnknownHostException
                || thr instanceof SocketTimeoutException;
    }

    private enum HttpError {
        ERROR_UNKNOWN, ERROR_CANCEL, ERROR_NO_NETWORK, ERROR_TIME_OUT, ERROR_URL, ERROR_PARSE_JSON, ERROR_REQUEST
    }

    private static class HttpErrorInfo {
        public HttpError httpError;
        public String msg;
    }
}
