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
 *    desc   : 避免重复执行
 *
 *             仅用于 Presenter 接口
 *
 *             某些耗时操作需要执行完毕之后, 才能重复执行.
 *             使用该注解, 可以在该方法执行的过程中, 再次执行将会自动进行取消.
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AvoidRepeating {
}
