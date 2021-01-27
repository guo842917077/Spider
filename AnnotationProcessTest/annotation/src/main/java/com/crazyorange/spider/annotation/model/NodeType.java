package com.crazyorange.spider.annotation.model;

/**
 * @author guojinlong01
 * 节点类型
 */
public enum NodeType {
    ACTIVITY(0, "android.app.Activity");

    int id;
    String className;

    NodeType(int id, String className) {
        this.id = id;
        this.className = className;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
