package com.test.day04;

/**
 * @author xiaocong
 * @date 2021/7/9 0009 - 16:33
 * 练习02
 * 要求验证是不是整数或者小数
 */
public class RegExpTest02 {
    public static void main(String[] args){

        //需考虑正数/负数
        //String content = "123 -345 34.89 -87.9 -0.01 0.45";
        //String content = "-234";
        //String regStr = "^[+-]?\\d+(\\.\\d+)?";
        //0089 认为格式错误 ---->如果是整数,开头必须是1-9
        //02.39 格式错误 ---->如果是小数,.前面只有一个0
        String content = "0.89";
        String regStr = "^[+-]?([1-9]\\d*|0)(\\.\\d+)?";

        //System.out.println(content.matches(regStr));
        if(content.matches(regStr)){
            System.out.println("匹配成功");
        }else{
            System.out.println("匹配失败");

        }
    }

}
