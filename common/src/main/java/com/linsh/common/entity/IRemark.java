package com.linsh.common.entity;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2022/02/21
 *    desc   : 备注
 * </pre>
 */
public interface IRemark {

    // 等级, 一组 ' ' 表示一级
    int getLevel();

    // 内容
    String getContent();

    // 子备注
    List<IRemark> getSubRemarks();

    // 是否存在加密信息
    boolean isEncrypted();
}
