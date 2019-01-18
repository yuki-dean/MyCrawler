package com.lf.mycrawler.link;

public interface LinkFilter {
    boolean accept(String url);
}
