package com.test.day04;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaocong
 * @date 2021/7/9 0009 - 16:55
 * 解析URL
 */
public class RegExpTest03 {
    public static void main(String[] args){
        //对一个URL进行解析
        String url = "https://www.sohu.com:8080/abc/index.html";
        //分组 -->分为4组
        String regStr = "^([a-zA-Z]+)://([a-zA-Z.]+):(\\d+)[\\w/-]*/([\\w-&%$?.]+)$";
        //a.要求得到协议是什么？ https
        //b.域名是 www.sohu.com
        //c.端口是 8080
        //d.文件名: index.html
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(url);
        while(matcher.find()){
            System.out.println("匹配到==>"+matcher.group(0));
            System.out.println("协议==>"+matcher.group(1));
            System.out.println("域名==>"+matcher.group(2));
            System.out.println("端口==>"+matcher.group(3));
            System.out.println("文件名==>"+matcher.group(4));

        }
    }
}
