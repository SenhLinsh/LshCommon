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

    void onRestore(IProperties properties);

    void onSave(IProperties properties);
}
