package com.test.day07;

import org.testng.Assert;

/**
 * @author xiaocong
 * @date 2021/7/15 0015 - 22:31
 * 包装类的相互转化
 */
public class PackageClassTest {
    public static void main(String[] args){
        //1.基本数据类型转化为包装类
        //自动装箱
        //int a = 10;
        //Integer b = a;
        //Integer bInt = 10;
        //System.out.println(bInt);
        ////2.包装类转为基本数据类型(拆箱)
        //Integer cInt = 100;
        //int c = cInt.intValue();
        //long d = cInt.longValue();
        //double f = cInt.doubleValue();
        //System.out.println("c::"+c);
        //System.out.println("d::"+d);
        //System.out.println("f::"+f);

        //3.字符串 转为  包装类(或基本数据类型)
        String aStr = "100";
        //3.1.字符串转为基本数据类型(包装类.parse基本数据类型(字符串))
        int aInt  = Integer.parseInt(aStr);
        //3.2.字符串转为包装类(包装类.valueOf(字符串))
        Integer bInt = Integer.valueOf(aStr);
        long aLong = Long.parseLong(aStr);
        Long bLong = Long.valueOf(aStr);
        System.out.println("aInt::"+aInt);
        System.out.println("bInt::"+bInt);
        System.out.println("along::"+aLong);
        System.out.println("blong::"+bLong);

        //将Integer类型转为Long
        //Integer a = 10;
        //Long b = Long.valueOf(a.toString());
        //Long c = 10l;
        //System.out.println(a.getClass());
        //System.out.println(b.getClass());
        //Assert.assertEquals(b,c);
    }
}
