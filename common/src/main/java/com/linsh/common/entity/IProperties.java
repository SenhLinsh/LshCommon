package com.linsh.common.entity;

import java.util.Map;
import java.util.Set;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2021/11/15
 *    desc   :
 * </pre>
 */
public interface IProperties {

    void put(String key, String value);

    void put(String key, int value);

    void put(String key, long value);

    void put(String key, String[] value);

    String get(String key);

    int getInt(String key);

    long getLong(String key);

    String[] getStringArray(String key);

    Set<Map.Entry<String, String>> entrySet();
}
