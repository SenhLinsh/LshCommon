package com.linsh.lshapp.common.adapter.converter;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * <pre>
 *    author : Senh Linsh
 *    date   : 2017/10/03
 *    desc   : 将 Retrofit 的 ResponseBody 转换成 String
 * </pre>
 */
public class StringConverterFactory extends Converter.Factory {

    private StringConverterFactory() {
    }

    public static StringConverterFactory create() {
        return new StringConverterFactory();
    }

    @Nullable
    @Override
    public Converter<ResponseBody, String> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new Converter<ResponseBody, String>() {
            @Override
            public String convert(ResponseBody value) throws IOException {
                return value.string();
            }
        };
    }
}
