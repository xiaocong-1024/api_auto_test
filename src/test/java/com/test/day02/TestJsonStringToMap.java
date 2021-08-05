package com.test.day02;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaocong
 * @date 2021/6/25 0025 - 17:52
 */
public class TestJsonStringToMap {

    public static void main(String[] args){

        String jsonStr = "{\"goods_id\":\"140861765\",\"cat_id\":\"210\",\"goods_sn\":\"171073501\",\"goods_sn_back\":\"171073501\",\"goods_upc\":null,\"goods_name\":\"Lace-Up Boxer Swimming Trunks\"}";

        //2.通过jackson依赖



        ////1.将一个jsonString转换为map对象
        ////使用fastjson
        //JSONObject jsonObject = JSONObject.parseObject(jsonStr);
        ////jsonObject --->Map<String,Object>
        //System.out.println(jsonObject.getString("goods_id"));
        //System.out.println(jsonObject.getString("cat_id"));
        //System.out.println(jsonObject.getString("goods_sn"));
        //System.out.println(jsonObject.getString("goods_upc"));

    }
}
