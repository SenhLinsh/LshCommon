package com.linsh.common.entity;

import com.google.gson.internal.LinkedTreeMap;
import com.linsh.lshutils.utils.ArrayUtilsEx;
import com.linsh.lshutils.utils.NumberUtilsEx;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2021/11/15
 *    desc   : IProperties 的实现类
 * </pre>
 */
public class Properties implements IProperties {

    private final Map<String, String> map = new LinkedTreeMap<>();

    @Override
    public void put(String key, String value) {
        if (value != null) {
            map.put(key, value);
        }
    }

    @Override
    public void put(String key, int value) {
        if (value != 0) {
            map.put(key, String.valueOf(value));
        }
    }

    @Override
    public void put(String key, long value) {
        if (value != 0) {
            map.put(key, String.valueOf(value));
        }
    }

    @Override
    public void put(String key, String[] value) {
        if (value != null) {
            map.put(key, Arrays.toString(value));
        }
    }

    @Override
    public String get(String key) {
        return map.get(key);
    }

    @Override
    public int getInt(String key) {
        return NumberUtilsEx.parseInt(map.get(key), 0);
    }

    @Override
    public long getLong(String key) {
        return NumberUtilsEx.parseLong(map.get(key), 0);
    }

    @Override
    public String[] getStringArray(String key) {
        return ArrayUtilsEx.fromString(map.get(key));
    }

    @Override
    public Set<Map.Entry<String, String>> entrySet() {
        return map.entrySet();
    }
}
