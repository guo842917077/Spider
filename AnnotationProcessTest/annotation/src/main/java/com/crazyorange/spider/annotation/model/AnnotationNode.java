package com.crazyorange.spider.annotation.model;

import com.crazyorange.spider.annotation.RouterPage;

import java.util.Map;

import javax.lang.model.element.Element;

/**
 * @author guojinlong01
 * 维护注解节点的抽象，绑定注解和 Element
 * 为什么没有像 ARouter 一样使用 SpiderNode 一种数据类型维护注解和 Element 的关系。也在编译时生成跳转信息使用？
 * 因为在编译时如果 SpiderNode 中引入了 Element 会报 can not reference Element 错误
 */
public class AnnotationNode {
    private NodeType type;
    private RouterPage pageAnnotation;
    private Element element;
    private Map<String, Integer> fields;

    public AnnotationNode(NodeType type, RouterPage pageAnnotation,
                          Element element, Map<String, Integer> params) {
        this.type = type;
        this.pageAnnotation = pageAnnotation;
        this.element = element;
        this.fields = params;
    }

    public AnnotationNode(NodeType type, RouterPage pageAnnotation,
                          Element element) {
        this.type = type;
        this.pageAnnotation = pageAnnotation;
        this.element = element;
    }

    public Map<String, Integer> getFields() {
        return fields;
    }

    public void setFields(Map<String, Integer> fields) {
        this.fields = fields;
    }

    public NodeType getType() {
        return type;
    }

    public void setType(NodeType type) {
        this.type = type;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public RouterPage getPageAnnotation() {
        return pageAnnotation;
    }

    public void setPageAnnotation(RouterPage pageAnnotation) {
        this.pageAnnotation = pageAnnotation;
    }

    @Override
    public String toString() {
        return "SpiderNode{" +
                "type=" + type +
                ", path='" + pageAnnotation.path() + '\'' +
                ", group='" + pageAnnotation.group() + '\'' +
                '}';
    }
}
