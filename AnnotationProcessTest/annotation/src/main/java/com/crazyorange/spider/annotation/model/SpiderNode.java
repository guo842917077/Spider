package com.crazyorange.spider.annotation.model;

/**
 * 节点 封装跳转对象的实例信息
 */
public class SpiderNode {
    private NodeType type;
    // 路径
    private String path;
    // 分组
    private String group;
    // 目标路径
    private Class<?> destination;

    public SpiderNode() {

    }

    public SpiderNode(NodeType type,
                      String path,
                      String group,  Class<?> destination) {
        this.type = type;
        this.path = path;
        this.group = group;
        this.destination = destination;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    @Override
    public String toString() {
        return "SpiderNode{" +
                "type=" + type +
                ", path='" + path + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
