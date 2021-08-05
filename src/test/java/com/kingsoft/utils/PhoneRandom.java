package com.kingsoft.utils;

import java.util.Random;

/**
 * @author xiaocong
 * @date 2021/7/13 0013 - 21:14
 * 随机生成一个手机号
 */
public class PhoneRandom {
    public static String getPhone(){
        String phonePrefix = "133";
        //循环拼接8位(0-9)随机数
        for(int i=0;i<8;i++){
            Random random = new Random();
            int value = random.nextInt(9);
            phonePrefix+=value;
        }
       return phonePrefix;
    }

    public static String getNoRegisterPhone(){
            //随机获取一个phone,判断数据库中是否存在,如果有,则继续随机生成phone,直到随机生成的手机号在数据库中不存在
        while(true){
            String phone = getPhone();
            String sql = "select count(*) from member where mobile_phone="+phone;
            Object result = JDBCUtils.querySingle(sql);
            if(result.equals(1)){
                //System.out.println("手机号已注册");
            }else{
                return phone;
            }
        }

    }

    //public static void main(String[] args){
    //   getNoRegisterPhone();
    //}
}
