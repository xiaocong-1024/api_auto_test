package com.kingsoft.testcases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingsoft.base.BaseCase;
import com.kingsoft.data.GlobalEnvironmentVar;
import com.kingsoft.pojo.CaseInfo;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @author xiaocong
 * @date 2021/7/15 0015 - 16:01
 * 充值接口(投资人)
 */
public class RechargeTest extends BaseCase {
    private List<CaseInfo> cases;
    @BeforeClass
    public void setUpTest(){
        //1.参数初始化
        cases = getCaseDatasFromExcel(3);
        //对用例进行参数化
        cases = paramsReplace(cases);
    }

    @Test(dataProvider="getCaseDatas")
    public void testRecharge(CaseInfo caseInfo) throws JsonProcessingException {
        //2.提取出请求体 请求体,发送请求
        //使用jackson将json格式的数据转换为map数据类型
        //ObjectMapper
        Map requestHeaders = fromJsonToMap(caseInfo.getRequestHeader());
        //从excel中读取期望值
        //rest-assured默认会将返回结果json里面的小数通过Float来存储
        String logFilePath = addLogToFile(caseInfo.getInterfaceName(), caseInfo.getCaseId());
        Response response=
                given().
                        log().all().
                        headers(requestHeaders).
                        body(caseInfo.getInputParams()).
                when().
                        post(caseInfo.getUrl()).
                then().
                        log().all().
                        extract().response();
        //将日志作为附件添加至allure报告中
        try {
            Allure.addAttachment("日志信息", new FileInputStream(logFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //接口响应断言
        assertExpected(caseInfo, response);
        //数据库断言
        assertSQL(caseInfo);
    }

    @DataProvider()
    public Object[] getCaseDatas(){
        return cases.toArray();
    }

}
