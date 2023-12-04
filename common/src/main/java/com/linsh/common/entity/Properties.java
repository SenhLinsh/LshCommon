package com.linsh.common.entity;

import com.google.gson.internal.LinkedTreeMap;
import com.linsh.base.entity.IProperties;
import com.linsh.lshutils.utils.ArrayUtilsEx;
import com.linsh.lshutils.utils.NumberUtilsEx;
import com.linsh.utilseverywhere.ListUtils;
import com.linsh.utilseverywhere.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        } else {
            map.remove(key);
        }
    }

    @Override
    public void put(String key, int value) {
        if (value != 0) {
            map.put(key, String.valueOf(value));
        } else {
            map.remove(key);
        }
    }

    @Override
    public void put(String key, long value) {
        if (value != 0) {
            map.put(key, String.valueOf(value));
        } else {
            map.remove(key);
        }
    }

    @Override
    public void put(String key, String[] value) {
        if (value != null) {
            map.put(key, Arrays.toString(value));
        } else {
            map.remove(key);
        }
    }

    @Override
    public void put(String key, List<?> value) {
        if (value != null) {
            map.put(key, ListUtils.toString(value));
        } else {
            map.remove(key);
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
    public String[] getArray(String key) {
        return ArrayUtilsEx.fromString(map.get(key));
    }

    @Override
    public String[] getArrayAsTag(String key) {
        String value = map.get(key);
        if (value != null) {
            value = value.trim();
            if (value.isEmpty()) {
                return null;
            }
            if (value.startsWith("#")) {
                value = value.substring(1);
            }
            return value.split(" *# *");
        }
        return null;
    }

    @Override
    public List<String> getList(String key) {
        String[] array = getArray(key);
        if (array != null) {
            return Arrays.asList(array);
        }
        return null;
    }

    @Override
    public List<String> getListAsTag(String key) {
        String value = map.get(key);
        if (value != null) {
            List<String> tags = new ArrayList<>();
            String[] args = value.split(" *# *");
            for (String arg : args) {
                if (StringUtils.notEmpty(arg)) {
                    tags.add(arg);
                }
            }
            return tags;
        }
        return null;
    }

    @Override
    public Set<Map.Entry<String, String>> entrySet() {
        return map.entrySet();
    }

    @Override
    public String remove(String key) {
        return map.remove(key);
    }
}
