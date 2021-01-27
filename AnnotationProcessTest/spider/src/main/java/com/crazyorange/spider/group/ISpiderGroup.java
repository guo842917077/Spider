package com.crazyorange.spider.group;


import com.crazyorange.spider.annotation.model.SpiderNode;

import java.util.Map;

/**
 * @author guojinlong01
 * @Date 2021-01-25
 * 组节点，负责维护该组下所有节点的信息
 */
public interface ISpiderGroup {
    void load(Map<String, SpiderNode> group);
}
