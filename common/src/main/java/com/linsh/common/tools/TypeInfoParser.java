package com.linsh.common.tools;

import androidx.annotation.NonNull;

import com.linsh.common.entity.IRemark;
import com.linsh.common.entity.IType;
import com.linsh.common.entity.Remark;
import com.linsh.common.entity.Type;
import com.linsh.utilseverywhere.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

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
        Type root = new Type(0, "root", null, false, false);
        int index = 0;
        while (index < lines.size()) {
            int level;
            String line = lines.get(index++);
            if ((level = lastIndexOf(line, '#')) > 0 || (level = lastIndexOf(line, '*')) > 0) {
                boolean isHide = line.startsWith("*");
                Type parent = (Type) getParentType(root, level);
                if (parent != null) {
                    line = line.substring(level).trim();
                    String[] args = line.split("[:：]", 2);
                    String name = args[0].trim();
                    String content = args.length > 1 ? args[1].trim() : "";
                    if (level < 2 || StringUtils.notEmpty(content)) {
                        if (parent.getSubTypes() == null) {
                            parent.setSubTypes(new ArrayList<>());
                        }
                        boolean encrypted = isEncrypted(content);
                        parent.getSubTypes().add(new Type(level, name, content, isHide, encrypted));
                    }
                }
            } else if (line.trim().startsWith("-")) {
                Type parentType = (Type) getParentType(root);
                if (parentType != null) {
                    level = lastIndexOf(line, ' ');
                    if (parentType.getRemarks() == null) {
                        parentType.setRemarks(new ArrayList<>());
                    }
                    List<IRemark> remarks = getParentRemarks(parentType.getRemarks(), level);
                    String remark = line.trim().substring(1).trim();
                    if (isEncrypted(remark)) {
                        remarks.add(new Remark(level, remark, true));
                    } else {
                        remarks.add(new Remark(level, remark, false));
                    }
                }
            }
        }
        return root.getSubTypes();
    }

    private static int lastIndexOf(String text, char c) {
        int index = 0;
        while (index < text.length() && text.charAt(index) == c) {
            index++;
        }
        return index;
    }

    private static IType getParentType(IType root, int level) {
        if (root == null || root.getSubTypes() == null || root.getSubTypes().size() == 0)
            return root;
        IType last = root.getSubTypes().get(root.getSubTypes().size() - 1);
        if (Math.abs(last.getLevel()) < level)
            return getParentType(last, level);
        return root;
    }

    private static IType getParentType(IType root) {
        if (root == null || root.getSubTypes() == null || root.getSubTypes().size() == 0)
            return root;
        return getParentType(root.getSubTypes().get(root.getSubTypes().size() - 1));
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

    public static String getStatusPersonPostFix(String personId) {
        String postFix = "";
        for (int i = personId.length() - 1; i >= 0; i--) {
            char c = personId.charAt(i);
            if (c == '*' || c == '$' || c == '+' || c == '#' || c == '?') {
                postFix = c + postFix;
            }
        }
        return postFix;
    }

    public static boolean isEncrypted(String text) {
        return Pattern.compile("[*%^&]>").matcher(text).find();
    }
}
