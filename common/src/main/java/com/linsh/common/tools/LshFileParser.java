package com.linsh.common.tools;

import com.linsh.base.LshLog;
import com.linsh.base.entity.IProperties;
import com.linsh.common.entity.Properties;
import com.linsh.lshutils.utils.StringUtilsEx;
import com.linsh.utilseverywhere.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/01
 *    desc   : 个人定义文件的解析器
 * </pre>
 */
public class LshFileParser {

    private static final String TAG = "LshFileParser";

    /**
     * 读取 Header
     */
    public static IProperties readHeader(ILshFileReader reader) {
        return readPropertiesBlock(reader, "header");
    }

    /**
     * 读取 Template
     */
    public static IProperties readTemplate(ILshFileReader reader) {
        return readPropertiesBlock(reader, "template");
    }

    /**
     * 读取连续的 IProperties
     */
    public static List<IProperties> readProperties(ILshFileReader reader) {
        ArrayList<IProperties> result = new ArrayList<>();
        IProperties properties;
        while ((properties = readNextProperties(reader)) != null) {
            result.add(properties);
        }
        return result;
    }

    /**
     * 读取头部 IProperties 区块
     * <p>
     * 格式为：
     * <----header
     * key: value
     * ------>
     *
     * @param blockName 区块名
     */
    private static IProperties readPropertiesBlock(ILshFileReader reader, String blockName) {
        reader.skipEmptyLine();
        String line = reader.getCurrentLine();
        if (line != null && StringUtils.trimBlank(line).matches("<(--+" + blockName + "|----+)")) {
            IProperties properties = new Properties();
            for (; reader.getCurrentLine() != null; reader.nextLine()) {
                line = StringUtils.nullToEmpty(reader.getCurrentLine()).trim();
                if (line.matches("----+>")) {
                    // ----> 为退出标志
                    reader.nextLine();
                    break;
                }
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
                    properties.put(name, StringUtils.trimBlank(line.substring(index + 1)));
                }
            }
            return properties;
        }
        return null;
    }

    /**
     * 读取下一个 IProperties
     */
    public static IProperties readNextProperties(ILshFileReader reader) {
        reader.skipEmptyLine();
        IProperties properties = null;
        for (; reader.getCurrentLine() != null; reader.nextLine()) {
            String line = StringUtils.nullToEmpty(reader.getCurrentLine()).trim();
            if (line.length() == 0) {
                // 空行为退出标志
                break;
            }
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
                if (properties == null) {
                    properties = new Properties();
                }
                properties.put(name, StringUtils.trimBlank(line.substring(index + 1)));
            }
        }
        return properties;
    }
}
