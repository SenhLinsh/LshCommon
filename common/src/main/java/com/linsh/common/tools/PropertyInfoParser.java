package com.linsh.common.tools;

import androidx.annotation.NonNull;

import com.linsh.base.LshLog;
import com.linsh.common.entity.IProperties;
import com.linsh.common.entity.IPropertyInfo;
import com.linsh.common.entity.Properties;
import com.linsh.lshutils.utils.StringUtilsEx;
import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.utilseverywhere.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2021/11/15
 *    desc   : Info 文件解析和格式化
 * </pre>
 */
public class PropertyInfoParser {

    private static final String TAG = "PropertyInfoParser";

    /**
     * 解析 Info 文件行
     */
    public static <T extends IPropertyInfo> T parse(Class<T> infoClass, List<String> lines) throws Exception {
        IProperties properties = parse(lines);
        T instance = (T) ClassUtils.newInstance(infoClass);
        instance.setProperties(properties);
        return instance;
    }

    /**
     * 解析 Info 文件行
     */
    public static <T extends IPropertyInfo> T parse(Class<T> infoClass, String content) throws Exception {
        IProperties properties = parse(Arrays.asList(content.split("\n")));
        T instance = (T) ClassUtils.newInstance(infoClass);
        instance.setProperties(properties);
        return instance;
    }

    /**
     * 解析 Info 文件行
     */
    @NonNull
    private static IProperties parse(List<String> lines) {
        IProperties result = new Properties();
        for (int i = 0; i < lines.size(); i++) {
            String line = StringUtils.trimBlank(lines.get(i));
            if (line.startsWith("#")) {
                // 注释行
                continue;
            }
            int index = StringUtilsEx.indexOf(line, ':', '：');
            if (index > 0) {
                String name = StringUtils.trimBlank(line.substring(0, index));
                if (name.length() == 0) {
                    // 异常行
                    LshLog.w(TAG, "invalid line: " + line);
                    continue;
                }
                String value = StringUtils.trimBlank(line.substring(index + 1));
                if (value.length() == 0) {
                    // 值为空
                    continue;
                }
                result.put(name, value);
            }
        }
        return result;
    }

    /**
     * 格式化 PropertyInfo
     */
    @NonNull
    public static List<String> format(@NonNull IPropertyInfo propertyInfo) {
        Properties properties = new Properties();
        propertyInfo.getProperties(properties);
        return format(properties);
    }

    /**
     * 格式化 PropertyInfo
     */
    @NonNull
    public static List<String> format(@NonNull IProperties properties) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            list.add(entry.getKey() + "：" + entry.getValue());
        }
        return list;
    }
}
