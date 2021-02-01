package com.crazyorange.spider.annotation.model;

import java.util.Map;

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
    /**
     * 收集该类中使用了 ParamInject 注解的参数
     */
    private Map<String, Integer> params;

    public SpiderNode() {

    }

    public SpiderNode(NodeType type,
                      String path,
                      String group, Class<?> destination, Map<String, Integer> params) {
        this.type = type;
        this.path = path;
        this.group = group;
        this.destination = destination;
        this.params = params;
    }


    public SpiderNode(NodeType type,
                      String path,
                      String group, Class<?> destination) {
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

    public Map<String, Integer> getParams() {
        return params;
    }

    public void setParams(Map<String, Integer> params) {
        this.params = params;
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
