package com.lf.mycrawler.util;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class RegexRule {

    private ArrayList<String> positive = new ArrayList<String>();
    private ArrayList<String> negative = new ArrayList<String>();

    public RegexRule() {

    }
    public RegexRule(String rule) {
        addRule(rule);
    }

    public RegexRule(ArrayList<String> rules) {
        for (String rule : rules) {
            addRule(rule);
        }
    }

    public boolean isEmpty() {
        return positive.isEmpty();
    }

    /**
     * 添加一个正则规则，正则规则有两种，正正则和反正则。
     * URL符合正正则需要满足一下两个条件：1.至少匹配一条正则； 2.不能和任何反正则匹配
     * 正正则示例：+a.*c是一条正正则，正则的内容为a.*c，起始加号表示正正则
     * 反正则示例：-a.*c是一条反正则，正则的内容为a.*c，起始减号表示反正则
     * 如果一个规则的起始字符不为加号也不为减号，则该正则为正正则，正则的内容为自身
     * 例如：a.*c是一条正正则，正则的内容为a.*c
     * @param rule 正则规则
     * @return 自身
     */
    public RegexRule addRule(String rule) {
        if (rule.length() == 0) {
            return this;
        }
        char pn = rule.charAt(0); // 获取字符串第一个字符
        String realRule = rule.substring(1);
        if (pn == '+') {
            addPositive(realRule);
        } else if (pn == '-') {
            addNegative(realRule);
        } else {
            addPositive(realRule);
        }
        return this;
    }

    /**
     * 添加一个正则规则
     * @param positiveRegex
     * @return 自身
     */
    public RegexRule addPositive(String positiveRegex) {
        positive.add(positiveRegex);
        return this;
    }

    /**
     * 添加一个反则规则
     * @param negativeRegex
     * @return 自身
     */
    public RegexRule addNegative(String negativeRegex) {
        positive.add(negativeRegex);
        return this;
    }

    /**
     * 判断输入的字符串是否符合正则规则
     * @param str 输入的字符串
     * @return
     */
    public boolean isSatisfy(String str) {

        int state = 0;
        for (String nregex : negative) {
            if (Pattern.matches(nregex, str)) {
                return false;
            }
        }
        int count = 0;
        for (String prgex : positive) {
            if (Pattern.matches(prgex, str)) {
                count++;
            }
        }
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }
}
