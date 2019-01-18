package com.lf.mycrawler;

import com.lf.mycrawler.link.LinkFilter;
import com.lf.mycrawler.link.Links;
import com.lf.mycrawler.page.Page;
import com.lf.mycrawler.page.PageParserTool;
import com.lf.mycrawler.page.RequestAndResposeTool;
import com.lf.mycrawler.util.FileTool;
import org.jsoup.select.Elements;

import java.util.Set;

public class CrawlerMain {

    /**
     * 使用种子初始化 URL 队列
     * @param seeds 种子URL
     */
    private void initCralerWithSeeds(String[] seeds) {
        for (int i = 0; i < seeds.length; i++) {
            Links.addUnvisitedUrlQueue(seeds[i]);
        }
    }

    /**
     * 抓取过程
     * @param seeds
     */
    public void crawling(String[] seeds) {
        // 初始化 URL队列
        initCralerWithSeeds(seeds);
        // 定义过滤器，提取以****** 为开头的链接
        LinkFilter filter = new LinkFilter() {
            public boolean accept(String url) {
                if (url.startsWith("http://www.baidu.com")) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        // 循环条件： 待抓取的链接不空且抓取的网页不多于1000
        while (!Links.unVisitedUrlQueueIsEmpty() && Links.getVisitedUrlNum() <= 1000) {
            // 先从待访问的序列中取出第一个
            String visitUrl = (String)Links.removeHeadOfUnvisitedUrlQueue();
            if (visitUrl == null) {
                continue;
            }

            // 根据url 得到page
            Page page = RequestAndResposeTool.sendRequestAndGetResponse(visitUrl);
            // 对page 进行处理：访问DOM的某个标签
            Elements es = PageParserTool.select(page, "a");
            if (!es.isEmpty()) {
                System.out.println("下面将打印a标签：");
                System.out.println(es);
            }

            // 保存文件
            FileTool.saveTolocal(page);

            // 将已访问过的链接放入到已访问的链接集合中
            Links.addVisitedUrlSet(visitUrl);

            // 得到超链接
            Set<String> links = PageParserTool.getLinks(page, "img");
            for (String link : links) {
                Links.addUnvisitedUrlQueue(link);
                System.out.println("新增爬取路径：" + link);
            }
        }
    }

    // main()
    public static void main(String[] args){
        CrawlerMain myCrawler = new CrawlerMain();
        myCrawler.crawling(new String[]{"http://www.baidu.com"});
    }

}
