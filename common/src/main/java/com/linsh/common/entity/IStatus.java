package com.linsh.common.entity;

import java.util.List;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2023/03/01
 *    desc   : 格式接口：状态
 *
 *             用于发布或记录简单连续的信息，如状态、说说、日志等
 *
 *             状态内容由多个 IProperties 组成，单次状态可以理解为一个完整的 IProperties。
 * </pre>
 */
public interface IStatus {

    IProperties getHeader();

    IProperties getTemplate();

    List<IProperties> getContents();
}