package com.test.day01;

import static io.restassured.RestAssured.*;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaocong
 * @date 2021/6/24 0024 - 12:08
 */
public class RESTAssuredApiTest {


    @Test
    public void testGet01(){
        // 1.简单地get请求
        given().
                //given配置请求参数/请求头/请求体数据
        when().
                //when是用来发起请求 GET/POST
                get("http://httpbin.org/get").
        then().
                //then是用来对响应结果进行处理
                log().all();
    }

    @Test
    public void testGet02(){
        //2.带参数的get请求
        given().
                queryParam("name", "张三").queryParam("age", "20").
                //given配置请求参数/请求头/请求体数据
        when().
                //when是用来发起请求 GET/POST
                get("http://httpbin.org/get").
        then().
                //then是用来对响应结果进行处理
                log().body();
    }

    @Test
    public void testGet03(){
        //3.带参数的get请求(多参数的情况)
        //given().when().get("http://httpbin.org/get").then().log().body();
        Map<String,String> map = new HashMap<String,String>();
        map.put("name", "zhangsan");
        map.put("sex","男");
        map.put("address","陕西");
        map.put("email","103284217@qq.com");

        given().
                queryParams(map).
        when().
                get("http://httpbin.org/get").
        then().
                log().body();
    }

    //post发送form表单
    @Test
    public void testPost01(){
        //given().when().post("http://httpbin.org/post").then().log().body();
        Map<String,String> map = new HashMap<String,String>();
        map.put("name", "zhangsan");
        map.put("sex","男");
        map.put("address","陕西");
        map.put("email","103284217@qq.com");
        given().
                //formParam("name", "zhangsan").
                formParams(map).
                contentType("application/x-www-form-urlencoded;charset=utf-8").
        when().
                post("http://httpbin.org/post").
        then().
                log().body();
    }

    //post发送json数据
    @Test
    public void testPost02(){
        String jsonStr = "{\"name\":\"张三\",\"mobilephone\":\"15202997202\"}";
        given().
                body(jsonStr).
                contentType("application/json;charset=utf-8").
        when().
                post("http://httpbin.org/post").
        then().
                log().body();
    }
    //post发送xml类型数据
    @Test
    public void testPost03(){
        String xmlStr = "<?xml version=\"1.0\" encoding=\"utf-8\">\n" +
                "\t<suite>\n" +
                "\t\t<class>测试xml</class>\n" +
                "\t</suite>";
        given().
                body(xmlStr).
                contentType("text/xml;charset=utf-8").
        when().
                post("http://httpbin.org/post").
        then().
                log().body();
    }
    //post 请求--->多参数表单(传输大量数据/文件时)
    @Test
    public void testPost04(){
        given().
                multiPart(new File("G:\\rename.txt")).
                contentType("multipart/form-data;charset=utf-8").
        when().
                post("http://httpbin.org/post").
        then().
                log().body();
    }
}
