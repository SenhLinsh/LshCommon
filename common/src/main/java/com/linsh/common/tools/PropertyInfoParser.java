package com.linsh.common.tools;

import androidx.annotation.NonNull;

import com.linsh.base.LshLog;
import com.linsh.base.entity.IProperties;
import com.linsh.base.entity.IPropertyInfo;
import com.linsh.common.entity.Properties;
import com.linsh.lshutils.utils.StringUtilsEx;
import com.linsh.utilseverywhere.ClassUtils;
import com.linsh.utilseverywhere.StringUtils;

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
     * 构建属性
     */
    public static IProperties build() {
        return new Properties();
    }

    /**
     * 解析 Info 文件行
     */
    public static <T extends IPropertyInfo> T parse(@NonNull Class<T> infoClass, @NonNull List<String> lines) throws Exception {
        IProperties properties = parse(lines);
        T instance = (T) ClassUtils.newInstance(infoClass);
        instance.onRestore(properties);
        return instance;
    }

    /**
     * 解析 Info 文件行
     */
    public static <T extends IPropertyInfo> T parse(@NonNull Class<T> infoClass, @NonNull String content) throws Exception {
        IProperties properties = parse(Arrays.asList(content.split("\n")));
        T instance = (T) ClassUtils.newInstance(infoClass);
        instance.onRestore(properties);
        return instance;
    }

    /**
     * 解析 Info 文件内容
     */
    public static IProperties parse(@NonNull String content) {
        return parse(Arrays.asList(content.split("\n")));
    }

    /**
     * 解析 Info 文件行
     */
    @NonNull
    public static IProperties parse(@NonNull List<String> lines) {
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
                result.put(name, StringUtils.trimBlank(line.substring(index + 1)));
            }
        }
        return result;
    }

    /**
     * 格式化 IPropertyInfo
     */
    @NonNull
    public static String format(@NonNull IPropertyInfo propertyInfo) {
        Properties properties = new Properties();
        propertyInfo.onSave(properties);
        return format(properties);
    }

    /**
     * 格式化 PropertyInfo
     */
    @NonNull
    public static String format(@NonNull IProperties properties) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : properties.entrySet()) {
            if (builder.length() > 0) builder.append("\n");
            builder.append(entry.getKey()).append("：").append(entry.getValue());
        }
        return builder.toString();
    }
}
