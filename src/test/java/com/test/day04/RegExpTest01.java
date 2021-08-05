package com.test.day04;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaocong
 * @date 2021/7/9 0009 - 16:14
 * 练习24
 * 验证电子邮件格式是否合法
 */
public class RegExpTest01 {
    public static void main(String[] args){
        //规定电子邮件规则为
        //1.只能有一个@
        //2.@前面是用户名，可以是a-z A-Z 0-9 _ -字符
        //3.@后面是域名,并且域名只能是英文字母,比如sohu.com或tsinghua.org.cn
        //4.写出对应的正则表达式,验证输入的字符串是否满足规则
        //String content = "wangxiao@kingsoft.com";
        String content = "wangxiao@tsinghua.org.cn";
        String regStr = "^[\\w-]+@([a-zA-Z]+\\.)+[a-zA-Z]+$";

        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);
        //matcher.matches()整体匹配
        System.out.println("是否匹配:"+matcher.matches());
    }

}
