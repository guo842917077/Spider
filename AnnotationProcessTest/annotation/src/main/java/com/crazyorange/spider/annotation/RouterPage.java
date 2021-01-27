package com.crazyorange.spider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface RouterPage {
    // 要跳转的 Path
    String path();

    /**
     * 设置组别，在生成路由时会根据组别进行合并分类
     */
    String group();

    /**
     * 设置
     */
    String key() default "";
}
