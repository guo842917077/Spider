package com.crazyorange.spider.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记参数 跳转到其它页面时会根据 key 去寻找对应的 value
 *
 * @author guojinlong01
 * @Date 2021-01-11
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface AutoInject {
    // Mark param's name
    String key() default "";

    // If required, app will be crash when value is null.
    // If not required, wont be check!
    boolean required() default false;

    // Description of the field
    String desc() default "";
}
