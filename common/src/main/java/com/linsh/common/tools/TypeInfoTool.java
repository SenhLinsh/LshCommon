package com.linsh.common.tools;

import com.linsh.base.entity.IType;
import com.linsh.lshutils.utils.ArrayUtilsEx;
import com.linsh.lshutils.utils.StringUtilsEx;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/02/24
 *    desc   :
 * </pre>
 */
public class TypeInfoTool {

    public static final Character[] CHARACTERS_LEVEL = {'#', '*'};

    /**
     * 解析当前行数据的层级
     *
     * @param line 行数据
     */
    public static int parseLevel(String line) {
        int index = 0;
        while (index < line.length() && ArrayUtilsEx.contains(CHARACTERS_LEVEL, line.charAt(index))) {
            index++;
        }
        return index;
    }

    /**
     * 格式化当前层级，使用 # 或 * 表示
     */
    public static String formatLevel(IType type) {
        return StringUtilsEx.jointStr(type.isHidden() ? "*" : "#", type.getLevel());
    }

    /**
     * 当前行是否是隐藏类型
     */
    public static boolean isHide(String line) {
        return line.startsWith("*");
    }

    /**
     * 当前行是否是加密类型
     */
    public static boolean isEncrypted(String line) {
        return Pattern.compile("[*%^&]>").matcher(line).find();
    }

    /**
     * 全文中查找指定类型的位置
     *
     * @param text       全文
     * @param type       类型
     * @param startIndex 开始查找位置
     */
    public static int findIndex(String text, IType type, int startIndex) {
        String prefix = formatLevel(type) + " " + type.getName();
        if (startIndex == 0 && text.startsWith(prefix)) {
            return 0;
        }
        int index = text.indexOf("\n" + prefix, startIndex);
        if (index >= 0) {
            return index + 1;
        }
        return index;
    }

    /**
     * 全文中查找指定层级的位置
     *
     * @param text       全文
     * @param level      层级
     * @param startIndex 开始查找位置
     * @return
     */
    public static int findIndex(String text, int level, int startIndex) {
        if (startIndex == 0) {
            if (parseLevel(text) == level) {
                return 0;
            }
            return -1;
        }
        Matcher matcher = Pattern.compile("\n([#*]+) ").matcher(text);
        while (matcher.find(startIndex)) {
            int targetLevel = matcher.group(1).length();
            if (targetLevel == level) {
                return matcher.start(1);
            }
            if (targetLevel < level) {
                return matcher.start();
            }
            startIndex = matcher.end();
        }
        return -1;
    }
}
