package com.lf.mycrawler.link;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * 存储已访问的URL路径和待访问的URL路径
 */
public class Links {
    // 已访问的url集合  主要考虑：集合中的元素不重复，用set来保证不重复
    private static Set visitedUrlSet = new HashSet();

    // 待访问的url集合  主要考虑：1. 访问的顺序； 2. 保证不提供重复的待访问地址。
    private static LinkedList unVisitedUrlQueue = new LinkedList();

    // 获得已访问的url数目
    public static int getVisitedUrlNum() {
        return visitedUrlSet.size();
    }

    // 添加到访问过的url
    public static void addVisitedUrlSet(String url) {
        visitedUrlSet.add(url);
    }

    // 移除访问过的url
    public static void removeVistedUrlSet(String url) {
        visitedUrlSet.remove(url);
    }


    // 获得待访问的url集合
    public static LinkedList getUnVisitedUrlQueue() {
        return unVisitedUrlQueue;
    }

    // 添加到待访问的集合中，保证每个url只被访问一次
    public static void addUnvisitedUrlQueue(String url) {
        if (url != null && !url.trim().equals("") && !visitedUrlSet.contains(url) && !unVisitedUrlQueue.contains(url)) {
            unVisitedUrlQueue.add(url);
        }
    }

    // 删除待访问的url
    public static Object removeHeadOfUnvisitedUrlQueue() {
        return unVisitedUrlQueue.removeFirst();
    }

    // 判断未访问的url队列是否为空
    public static boolean unVisitedUrlQueueIsEmpty() {
        return unVisitedUrlQueue.isEmpty();
    }
}
