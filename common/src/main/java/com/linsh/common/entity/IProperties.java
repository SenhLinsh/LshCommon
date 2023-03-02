package com.linsh.common.entity;

import java.util.Map;
import java.util.Set;

import io.realm.RealmList;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2021/11/15
 *    desc   : Properties 文件声明类
 *
 *             该文件类型目前用于：
 *                 1. 朝花夕逝图片信息
 * </pre>
 */
public interface IProperties {

    void put(String key, String value);

    void put(String key, int value);

    void put(String key, long value);

    void put(String key, String[] value);

    void put(String key, RealmList<?> value);

    String get(String key);

    int getInt(String key);

    long getLong(String key);

    String[] getArray(String key);

    String[] getArrayAsTag(String key);

    RealmList<String> getRealmList(String key);

    RealmList<String> getRealmListAsTag(String key);

    Set<Map.Entry<String, String>> entrySet();

    String remove(String key);
}
