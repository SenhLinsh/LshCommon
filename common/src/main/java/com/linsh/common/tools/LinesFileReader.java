package com.linsh.common.tools;

import androidx.annotation.NonNull;

import com.linsh.utilseverywhere.StringUtils;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/01
 *    desc   : 行数据读取器
 * </pre>
 */
public class LinesFileReader implements ILshFileReader {

    private final List<String> lines;
    private int index;

    public LinesFileReader(@NonNull List<String> lines) {
        this.lines = lines;
    }

    /**
     * 获取原始内容
     */
    public List<String> getContents() {
        return lines;
    }

    /**
     * 获取指定行内容
     *
     * @param index 行索引
     */
    public String getLine(int index) {
        if (index < 0 || index >= lines.size())
            return null;
        return lines.get(index);
    }

    /**
     * 获取当前行数据
     */
    public String getCurrentLine() {
        return getLine(index);
    }

    /**
     * 获取当前行索引
     */
    public int getCurrentIndex() {
        return index;
    }

    /**
     * 下一行
     */
    public boolean nextLine() {
        if (index >= lines.size())
            return false;
        index++;
        return true;
    }

    /**
     * 跳过空行
     */
    public boolean skipEmptyLine() {
        String line;
        boolean ret = false;
        while ((line = getCurrentLine()) != null && StringUtils.trimBlank(line).length() == 0 && nextLine()) {
            ret = true;
        }
        return ret;
    }
}
