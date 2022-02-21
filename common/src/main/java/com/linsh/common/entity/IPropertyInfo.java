package com.linsh.common.entity;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2021/11/15
 *    desc   :
 * </pre>
 */
public interface IPropertyInfo {

    /**
     * 由 info 文件解析恢复为 Bean 对象
     */
    void onRestore(IProperties properties);

    /**
     * 由 Bean 对象格式化保存为 info 文件
     */
    void onSave(IProperties properties);
}
