package com.crazyorange.spider.annotation;

/**
 * @author guojinlong01
 * @Date 注入参数
 */
public @interface ParamInject {
    String key() default "";

    /**
     * 如果是必备的参数 没有设置的话 运行时查询不到会崩溃
     */
    boolean required() default false;
}
