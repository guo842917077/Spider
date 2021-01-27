package com.crazyorange.spider.processor.contants;

public class Constant {
    /**
     * 分隔符
     */
    public static final String SEPARATOR = "$$";
    public static final String PROJECT = "Spider";

    public static final String GROUP_PACKAGE = "com.crazyorange.spider.group";
    public static final String GROUP_INTERFACE_ISPIDER_GROUP =
            "com.crazyorange.spider.group.ISpiderGroup";
    public static final String ACTIVITY_TYPE = "android.app.Activity";
    public static final String FRAGMENT_TYPE = "android.app.Fragment";
    public static final String FRAGMENT_V4_TYPE = "android.support.v4.app.Fragment";
    /**
     * 生成代码所需要的字段
     */
    public static final String METHOD_LOAD = "load";
    public static final String PARAM_GROUP_NAME = "container";
    public static final String NAME_OF_GROUP = PROJECT + SEPARATOR + "Group" + SEPARATOR;
}
