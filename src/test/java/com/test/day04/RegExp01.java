package com.test.day04;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaocong
 * @date 2021/7/9 0009 - 10:21
 * 实例练习
 * URL匹配
 */
public class RegExp01 {
    public static void main(String[] args){
        //1.汉字
        //String content01 = "韩顺平教育";
        //String regStr = "^[\\u4e00-\\u9fa5]+$"; //以汉字开头,且以汉字结尾

        //2.邮政编码
        //要求：匹配以1-9开头的一个六位数字
        //String content = "123456";
        //String regStr = "^[1-9]\\d{5}$";

        //3.qq号码
        //要求:匹配以1-9开头的一个(5位数-10位数)
        //String content = "1032852151";
        //String content = "10328";
        //String regStr = "^[1-9]\\d{4,9}";
        //4.手机号码
        //要求：必须以13,14,15，18开头的11位数
        //String content = "152029a97202";
        ////String content = "16789";
        //String regStr = "^1[3|4|5|8]\\d{10}";

        //5.验证url
        String content1 = "https://www.baidu.com" +
                "/s?" +
                "wd=%E6%9F%A0%E6%AA%AC%E7%8F%AD&rsv_spt=1" +
                "&rsv_iqid=0xcb47106a00085352&" +
                "issp=1&f=8&rsv_bp=1" +
                "&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_dl=tb&rsv_sug3=14&rsv_sug1=10&rsv_sug7=101&rsv_sug2=0&rsv_btype=i&inputT=2994&rsv_sug4=4710&rsv_sug=1";

        //思路:
        //1，先确定url的开始部分  http:// | https://
        //String content = "https://";
        //String regStrPar01 = "^((http|https)://)?";
        //2.然后根据              匹配 www.baidu.com(有可能是域名也有可能是ip)
        //String content = "www.baidu.com";
        //String regStrPar02 = "([\\w-]+\\.)+[\\w-]+";
        //3.通过       匹配后面的参数部分
        //String content = "/s?" +
        //        "wd=%E6%9F%A0%E6%AA%AC%E7%8F%AD&rsv_spt=1" +
        //        "&rsv_iqid=0xcb47106a00085352&" +
        //        "issp=1&f=8&rsv_bp=1" +
        //        "&rsv_idx=2&ie=utf-8&tn=baiduhome_pg&rsv_enter=1&rsv_dl=tb&rsv_sug3=14&rsv_sug1=10&rsv_sug7=101&rsv_sug2=0&rsv_btype=i&inputT=2994&rsv_sug4=4710&rsv_sug=1";
        //String regStrPar03 = "\\/[\\w?=%&./-]*";

        String content = "https://www.bilibili.com/video/BV1fh411y7R8";
        String regStr = "^((http|https)://)?([\\w-]+\\.)+[\\w-]+\\/[\\w?=%&./-]*";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);

        if(matcher.find()){
            System.out.println("满足格式!");
            System.out.println("找到=>"+matcher.group(0));
        }else{
            System.out.println("不满足格式!");
        }
        //while(matcher.find()){
        //    System.out.println("找到=>"+matcher.group(0));
        //}

    }
}
