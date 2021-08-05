package com.test.day07;

import org.testng.Assert;

import java.math.BigDecimal;

/**
 * @author xiaocong
 * @date 2021/7/15 0015 - 16:12
 */
public class BigDecimalTest {
    public static void main(String[] args){
        Double a = 0.1;
        Float b = 0.1f;
        //Double b = 0.1;
        BigDecimal bigDecimala = new BigDecimal(a.toString());
        BigDecimal bigDecimalb = new BigDecimal(b.toString());
        //System.out.println(a.getClass());
        //System.out.println(b.getClass());
        Assert.assertEquals(bigDecimala, bigDecimalb);
    }
}
