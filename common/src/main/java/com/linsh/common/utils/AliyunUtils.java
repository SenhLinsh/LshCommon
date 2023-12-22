package com.linsh.common.utils;

import android.util.Base64;
import android.util.Log;

import java.util.Map;
import java.util.TreeMap;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/12/22
 *    desc   : 阿里云相关工具类
 * </pre>
 */
public class AliyunUtils {

    private static final String TAG = "AliyunUtils";

    /**
     * 阿里云HTTP协议签名
     * <p>
     * 对于阿里云的 HTTP API, 需要对请求进行签名, 以验证请求的合法性。其签名过程较为复杂，这里进行了封装.
     * <p>
     * 文档地址：https://help.aliyun.com/zh/vms/the-http-protocol-and-signature
     *
     * @param method       请求方法 GET/POST
     * @param params       请求参数，除了 Signature 之外的其他参数
     * @param accessSecret 阿里云 AccessSecret
     */
    public static String getSignature(String method, Map<String, String> params, String accessSecret) throws Exception {
        // 对参数进行排序
        TreeMap<String, String> sortedParams = new TreeMap<>(params);
        // 构造待签名的请求串
        StringBuilder sortQueryStringTmp = new StringBuilder();
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            sortQueryStringTmp.append("&").append(specialUrlEncode(key)).append("=").append(specialUrlEncode(value));
        }
        // 去除第一个多余的&符号
        String sortedQueryString = sortQueryStringTmp.substring(1);
        // 待签名的请求字符串
        String stringToSign = method + "&" +
                specialUrlEncode("/") + "&" +
                specialUrlEncode(sortedQueryString);
        // 打印 stringToSign，如果出现签名错误，服务器会返回 stringToSign，可以进行对比排查问题
        Log.d(TAG, "getSignature: stringToSign = " + stringToSign);
        // 生成签名
        return sign(accessSecret + "&", stringToSign);
    }

    private static String specialUrlEncode(String value) throws Exception {
        return java.net.URLEncoder.encode(value, "UTF-8")
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~");
    }

    private static String sign(String accessSecret, String stringToSign) throws Exception {
        javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA1");
        mac.init(new javax.crypto.spec.SecretKeySpec(accessSecret.getBytes("UTF-8"), "HmacSHA1"));
        byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
        return new String(Base64.encode(signData, Base64.NO_WRAP));
    }
}
