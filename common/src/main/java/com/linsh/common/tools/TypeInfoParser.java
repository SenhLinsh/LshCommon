package com.linsh.common.tools;

import androidx.annotation.NonNull;

import com.linsh.base.entity.IRemark;
import com.linsh.base.entity.IType;
import com.linsh.common.entity.Remark;
import com.linsh.common.entity.Type;
import com.linsh.lshutils.utils.ArrayUtilsEx;
import com.linsh.utilseverywhere.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2022/02/21
 *    desc   : 个人信息文件解析器
 * </pre>
 */
public class TypeInfoParser {

    /**
     * 解析 Info 文件行
     */
    @NonNull
    public static List<IType> parse(List<String> lines) {
        return parse(lines, true);
    }

    /**
     * 解析 Info 文件行
     *
     * @param lines               文件行数据
     * @param excludeEmptyContent 是否排除没有内容的类型（默认排除）
     */
    @NonNull
    public static List<IType> parse(List<String> lines, boolean excludeEmptyContent) {
        Stack<IType> levels = new Stack<>();
        levels.push(new Type(0, "root", null, false, false, false));
        for (String line : lines) {
            int level;
            // 根据 # * $ 个数来计算等级
            if ((level = TypeInfoTool.parseLevel(line)) > 0) {
                // * 代表隐藏类型
                boolean isHide = TypeInfoTool.isHide(line);
                // $ 代表重要类型
                boolean isImportant = TypeInfoTool.isImportant(line);
                // 获取父 Type
                Type parent = (Type) getParentType(levels, level);
                if (parent != null) {
                    line = line.substring(level).trim();
                    String[] args = line.split("[:：]", 2);
                    String name = args[0].trim();
                    String content = args.length > 1 ? args[1].trim() : "";
                    if (level < 2 || !excludeEmptyContent || StringUtils.notEmpty(content)) {
                        if (parent.getSubTypes() == null) {
                            parent.setSubTypes(new ArrayList<>());
                        }
                        boolean encrypted = TypeInfoTool.isEncrypted(content);
                        Type type = new Type(level, name, content, isHide, isImportant, encrypted);
                        parent.getSubTypes().add(type);
                        levels.push(type);
                    }
                }
                continue;
            }
            if (line.trim().startsWith("-")) {
                Type parentType = (Type) levels.peek();
                if (parentType != null) {
                    level = lastIndexOf(line, ' ');
                    if (parentType.getRemarks() == null) {
                        parentType.setRemarks(new ArrayList<>());
                    }
                    List<IRemark> remarks = getParentRemarks(parentType.getRemarks(), level);
                    String remark = line.trim().substring(1).trim();
                    if (TypeInfoTool.isEncrypted(remark)) {
                        remarks.add(new Remark(level, remark, true));
                    } else {
                        remarks.add(new Remark(level, remark, false));
                    }
                }
            }
        }
        return getParentType(levels, 1).getSubTypes();
    }

    private static int lastIndexOf(String text, Character... chars) {
        int index = 0;
        while (index < text.length() && ArrayUtilsEx.contains(chars, text.charAt(index))) {
            index++;
        }
        return index;
    }

    private static IType getParentType(Stack<IType> levels, int level) {
        while (levels.size() > level) {
            levels.pop();
        }
        return levels.peek();
    }

    private static List<IRemark> getParentRemarks(List<IRemark> remarks, int level) {
        if (remarks == null || remarks.size() == 0)
            return remarks;
        Remark last = (Remark) remarks.get(remarks.size() - 1);
        if (level > last.getLevel()) {
            if (last.getSubRemarks() == null)
                last.setSubRemarks(new ArrayList<>());
            return getParentRemarks(last.getSubRemarks(), level);
        }
        return remarks;
    }
}
