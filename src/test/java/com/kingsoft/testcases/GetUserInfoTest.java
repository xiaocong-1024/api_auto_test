package com.kingsoft.testcases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingsoft.base.BaseCase;
import com.kingsoft.pojo.CaseInfo;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;

/**
 * @author xiaocong
 * @date 2021/7/6 0006 - 1 7:41
 * 参数化封装 --->paramsReplace
 * 对excel中的请求头  接口地址  请求参数  期望返回结果做参数化
 */
public class GetUserInfoTest extends BaseCase {
    //private static final String BASEURL = "http://api.lemonban.com/futureloan";
    //private String getUserInfoUrl;
    private List<CaseInfo> cases;
    @BeforeClass
    public void setUpTest(){
        //1.参数初始化
        //cases --->从excel中取出的原始的case
        cases = getCaseDatasFromExcel(2);
        //cases --->参数化后的case
        cases = paramsReplace(cases);
    }

    @Test(dataProvider="getCaseDatas")
    public void testGetUserInfo(CaseInfo caseInfo) throws JsonProcessingException {
        //getUserInfoUrl = BASEURL + caseInfo.getUrl();

        String logFilePath = addLogToFile(caseInfo.getInterfaceName(), caseInfo.getCaseId());
        Map requestHeadersMap = fromJsonToMap(caseInfo.getRequestHeader());
        Response response=
        given().
                log().all().
                headers(requestHeadersMap).
        when().
                get(caseInfo.getUrl()).
        then().
                log().all().
                extract().response();
        //将日志作为附件添加至allure报告中
        try {
            Allure.addAttachment("日志信息", new FileInputStream(logFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //断言
        assertExpected(caseInfo, response);
    }

    @DataProvider()
    public Object[] getCaseDatas(){
        return cases.toArray();
    }


}
