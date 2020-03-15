package com.linsh.common.mvp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <pre>
 *    author : Senh Linsh
 *    github : https://github.com/SenhLinsh
 *    date   : 2019/12/18
 *    desc   : 执行方法期间使用 Loading 展示
 *
 *             建议用于 Presenter 接口.
 *
 *             某些耗时操作需要展示 Loading 来进行等待,
 *             使用该注解, 可以在该方法执行的前后自动展示和取消 Loading
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface WithLoading {
}