package com.linsh.common.tools;

import com.linsh.base.entity.IProperties;
import com.linsh.base.entity.IStatus;
import com.linsh.common.entity.Status;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/01
 *    desc   : IStatus 文本解析器
 * </pre>
 */
public class StatusInfoParser {

    /**
     * 解析 IStatus 文本
     *
     * @param lines 文本行内容
     */
    public static IStatus parse(List<String> lines) {
        LinesFileReader reader = new LinesFileReader(lines);
        IProperties headerProperties = LshFileParser.readHeader(reader);
        IProperties templateProperties = LshFileParser.readTemplate(reader);
        List<IProperties> contents = LshFileParser.readProperties(reader);
        return new Status(headerProperties, templateProperties, contents);
    }

    /**
     * 解析 IStatus 文本
     *
     * @param file 文件
     */
    public static IStatus parse(File file) {
        StreamFileReader reader = new StreamFileReader(file);
        IProperties headerProperties = LshFileParser.readHeader(reader);
        IProperties templateProperties = LshFileParser.readTemplate(reader);
        List<IProperties> contents = LshFileParser.readProperties(reader);
        reader.release();
        return new Status(headerProperties, templateProperties, contents);
    }

    /**
     * 预览 IStatus 文件
     * <p>
     * 读取 Header 区块 +
     */
    public static IStatus preview(File file) {
        StreamFileReader reader = new StreamFileReader(file);
        IProperties headerProperties = LshFileParser.readHeader(reader);
        IProperties templateProperties = LshFileParser.readTemplate(reader);
        IProperties content = LshFileParser.readNextProperties(reader);
        reader.release();
        return new Status(headerProperties, templateProperties, content != null ? Collections.singletonList(content) : null);
    }
}
