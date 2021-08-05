package com.test.day01;

import static io.restassured.RestAssured.*;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaocong
 * @date 2021/6/24 0024 - 16:31
 */
public class FutureLoanTest {

    String baseUrl = "http://api.lemonban.com/futureloan";
    Integer memberId = null;
    String tokenValue = "";
    Integer loanId = null;

    @Test(enabled = false)
    public void testRegister(){
        String registerUrl = baseUrl + "/member/register";
        Map<String,String> map = new HashMap<String,String>();
        map.put("mobile_phone","13333000010");
        map.put("pwd","12345678");
        map.put("type","0");
        Response res =
        given().
                header("X-Lemonban-Media-Type","lemonban.v1").
                contentType("application/json;charset=utf-8").
                body(map).
        when().
                post(registerUrl).
        then().
                extract().response();
        System.out.println(res.asString());
        Object value = res.path("data.id");
        System.out.println(value);
        //获取响应状态码
        //System.out.println(res.statusCode());
        //获取响应头信息
        //System.out.println(res.getHeader("Content-Type"));
        //获取接口的响应时间(接口耗时)
        //System.out.println(res.time());
    }

    //登录接口
    @Test
    public void testLogin01(){
        String loginUrl = baseUrl + "/member/login";
        Map<String,String> map = new HashMap<>();
        map.put("mobile_phone","13333000010");
        map.put("pwd","12345678");

        Response res =
        given().
                header("X-Lemonban-Media-Type","lemonban.v2").
                header("Content-Type","application/json;charset=utf-8").
                body(map).
        when().
                post(loginUrl).
        then().
                extract().response();
        System.out.println(res.asString());
        System.out.println((Object)res.path("code"));
        System.out.println((Object)res.path("msg"));
        memberId = res.path("data.id");
        System.out.println(memberId);
        tokenValue = res.path("data.token_info.token");
        //System.out.println((Object)res.path("data.token_info.token"));

    }



    //前提是：执行登录接口-->获取投资人id和token
    //充值 -->投资人
    @Test(enabled = false)
    public void testRecharge(){
        //充值接口
        String rechargeUrl = baseUrl + "/member/recharge";
        Map<String,String> rechargeMap = new HashMap<>();
        rechargeMap.put("member_id", memberId.toString());
        rechargeMap.put("amount","20000");

        Response rechargeRes =
                given().
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        header("Content-Type","application/json;charset=utf-8").
                        header("Authorization","Bearer "+tokenValue).
                        body(rechargeMap).
                when().
                        post(rechargeUrl).
                then().
                        extract().response();
        System.out.println(rechargeRes.asString());
        System.out.println((Object)rechargeRes.path("code"));
        System.out.println((Object)rechargeRes.path("msg"));

    }



    //添加项目(借款人) --->审核项目(管理员)
    @Test(priority = 1)
    public void testAddLoan(){
        String addLoanUrl = baseUrl + "/loan/add";
        Map<String,String> loanInfo = new HashMap<String,String>();
        //"member_id":1234614440,
        //        "title":"贷款开咖啡厅",
        //        "amount":"200000",
        //        "loan_rate":12.0,
        //        "loan_term":12,
        //        "loan_date_type":1,
        //        "bidding_days":5
        System.out.println("贷款人："+memberId);
        System.out.println("添加项目token:"+tokenValue);
        loanInfo.put("member_id",memberId+"");
        loanInfo.put("title","贷款开书屋");
        loanInfo.put("amount","200000");
        loanInfo.put("loan_rate","12.0");
        loanInfo.put("loan_term","12");
        loanInfo.put("loan_date_type","1");
        loanInfo.put("bidding_days","5");

        Response res =
                given().
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        header("Content-Type","application/json;charset=utf-8").
                        header("Authorization","Bearer "+tokenValue).
                        body(loanInfo).
                when().
                        post(addLoanUrl).
                then().
                        log().body().
                        extract().response();

        loanId = res.path("data.id");
        System.out.println(loanId);

        //审核项目
        //"loan_id":2101885,
        //"approved_or_not":true
        String auditLoanUrl = baseUrl + "/loan/audit";
        Map<String,String> map = new HashMap<>();
        map.put("loan_id",loanId+"");
        map.put("approved_or_not","true");
        Response auditLoanRes =
                given().
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        header("Content-Type","application/json;charset=utf-8").
                        header("Authorization","Bearer "+tokenValue).
                        body(map).
                when().
                        patch(auditLoanUrl).
                then().
                        log().body().
                        extract().response();


    }
    //投资(投资人)
    @Test(priority = 2)
    public void testInvest(){
        //"member_id":"1234614433",
        //        "loan_id":"2101885",
       //        "amount":"10000.0"
        String investUrl = baseUrl + "/member/invest";
        Map<String,String> map = new HashMap<>();
        map.put("member_id",memberId+"");
        map.put("loan_id",loanId+"");
        map.put("amount","10000");

        Response res =
                given().
                        header("X-Lemonban-Media-Type","lemonban.v2").
                        header("Content-Type","application/json;charset=utf-8").
                        header("Authorization","Bearer "+tokenValue).
                        body(map).
                when().
                        post(investUrl).
                then().
                        log().body().
                        extract().response();
    }

}
