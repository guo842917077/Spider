package com.crazyorange.spider.annotation.model;

/**
 * @author guojinlong01
 * @Date 2021-01-29
 * <p>
 * 标识数据的类型
 */
public enum ParamType {
    // Base type
    BOOLEAN,
    BYTE,
    SHORT,
    INT,
    LONG,
    CHAR,
    FLOAT,
    DOUBLE,

    // Other type
    STRING,
    SERIALIZABLE,
    PARCELABLE,
    OBJECT;
}
