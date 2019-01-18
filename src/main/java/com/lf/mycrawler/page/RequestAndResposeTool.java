package com.lf.mycrawler.page;


import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.IOException;

public class RequestAndResposeTool {

    public static Page sendRequestAndGetResponse(String url) {
        Page page = null;
        // 1. 生成 HttpClient 对象，并设置参数
        HttpClient httpClient = new HttpClient();
        // 设置HTTP连接超时5s
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        // 2. 生成GetMethod对象，并设置参数
        GetMethod getMethod = new GetMethod(url);
        // 设置 get 请求超时 5s
        getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 5000);
        // 设置请求重试处理
        getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
        // 3. 执行 HTTP GET 请求
        try {
            int statusCode = httpClient.executeMethod(getMethod);
        // 判断访问的状态码
            if (statusCode == HttpStatus.SC_OK) {
                System.err.println("Method failed: " + getMethod.getStatusLine());
            }
        // 4. 处理http 响应内容
            byte[] responseBody = getMethod.getResponseBody(); // 读取字节数组
            String contentType = getMethod.getResponseHeader("Content-Type").getValue(); // 得到当前返回类型
            page = new Page(responseBody, url, contentType); // 封装成页面
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        // 释放连接
            getMethod.releaseConnection();
        }
        return page;
    }
}
