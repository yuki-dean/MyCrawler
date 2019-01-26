package com.lf.mycrawler.page;

import com.lf.mycrawler.util.CharsetDetector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;

public class Page {

    private byte[] content;
    private String html;        //网页源码字符串
    private Document doc;       //网页Dom文档
    private String charset;     //字符编码
    private String url;         //url路径
    private String contentType; //内容类型

    public Page(byte[] content, String url, String contentType) {
        this.content = content;
        this.url = url;
        this.contentType = contentType;
    }

    public byte[] getContent() {
        return content;
    }

    /**
     *
     * @return 网页的源码字符串
     */
    public String getHtml() {
        if (html != null) {
            return html;
        }
        if (content == null) {
            return null;
        }
        if (charset == null) {
            // todo 根据内容来猜测字符编码
            charset = CharsetDetector.guessEncoding(content);
        }
        try {
            this.html = new String(content, charset);
            return html;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 得到文档
     * @return 文档
     */
    public Document getDoc() {
        if (doc != null) {
            return doc;
        }
        try {
            this.doc = Jsoup.parse(getHtml(), url);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCharset() {
        return charset;
    }

    public String getUrl() {
        return url;
    }

    public String getContentType() {
        return contentType;
    }
}
