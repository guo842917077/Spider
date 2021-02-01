package com.crazyorange.spider.group;


import java.util.Map;

/**
 * @author guojinlong01
 * @Date 2021-01-25
 * 负责维护所有组节点，优先将所有组节点加载到内存中，当使用某一个组中的内容时，再将这个组 load 到内存中
 * 避免了内存的浪费
 */
public interface ISpiderRoot {
    void load(Map<String, Class<? extends ISpiderGroup>> container);
}
