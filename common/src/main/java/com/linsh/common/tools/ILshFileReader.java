package com.linsh.common.tools;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/01
 *    desc   : 个人定义文件的解析器
 * </pre>
 */
interface ILshFileReader {

    /**
     * 获取当前行数据
     */
    String getCurrentLine();

    /**
     * 下一行
     */
    boolean nextLine();

    /**
     * 跳过空行
     */
    boolean skipEmptyLine();
}