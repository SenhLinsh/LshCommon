package com.linsh.common.entity;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2022/02/21
 *    desc   : 格式接口：类型
 *
 *            用于记录某事物的类型细节
 * </pre>
 */
public interface IType {

    // 类型等级, 一个 # 或 * 为一级
    int getLevel();

    // 名称
    String getName();

    // 内容
    String getContent();

    // 备注
    List<IRemark> getRemarks();

    // 子类型
    List<IType> getSubTypes();

    // 是否存在加密信息
    boolean isEncrypted();

    // 是否为隐藏项
    // # 为默认显示，* 为默认隐藏
    boolean isHidden();
}
