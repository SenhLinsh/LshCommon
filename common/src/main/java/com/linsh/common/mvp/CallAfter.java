package com.linsh.common.mvp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2020/03/15
 *    desc   : 结束后调用
 *
 *             仅用于 Presenter 接口, 且指定的方法必须为 View 的无参方法
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CallAfter {
    /**
     * @return 返回 View 接口的无参方法名
     * <p>
     * 注: 该方法必须为无参方法, 否则无法进行调用
     */
    String value();
}
