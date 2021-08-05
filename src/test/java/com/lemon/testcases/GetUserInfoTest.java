package com.lemon.testcases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemon.base.BaseCase;
import com.lemon.pojo.CaseInfo;
import com.lemon.data.GlobalEnvironmentVar;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

/**
 * @author xiaocong
 * @date 2021/7/6 0006 - 1 7:41
 * 参数化封装 --->paramsReplace
 * 对excel中的请求头  接口地址  请求参数  期望返回结果做参数化
 */
public class GetUserInfoTest extends BaseCase {
    private static final String BASEURL = "http://api.lemonban.com/futureloan";
    private String getUserInfoUrl;
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
        getUserInfoUrl = BASEURL + caseInfo.getUrl();

        ObjectMapper objectMapper = new ObjectMapper();
        Map requestHeadersMap = objectMapper.readValue(caseInfo.getRequestHeader(), Map.class);

        String expectedResult = caseInfo.getExpectedResult();
        Map expectedMap = objectMapper.readValue( expectedResult, Map.class);
        Set<Map.Entry<String,Object>> entrys = expectedMap.entrySet();
        //获得期望值(excel期望值中的key是响应体相应字段的Gpath表达式,value是Gpath表达式的值)
        Response response=
        given().
                headers(requestHeadersMap).
        when().
                get(getUserInfoUrl).
        then().
                //log().body().
                extract().response();

        //3.结果断言
        for(Map.Entry<String,Object> entry:entrys){
            Assert.assertEquals(response.path(entry.getKey()).toString(), entry.getValue().toString());
        }
    }

    @DataProvider()
    public Object[] getCaseDatas(){
        return cases.toArray();
    }


}
