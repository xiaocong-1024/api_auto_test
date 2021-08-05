package com.kingsoft.testcases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingsoft.base.BaseCase;
import com.kingsoft.data.GlobalEnvironmentVar;
import com.kingsoft.pojo.CaseInfo;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * @author xiaocong
 * @date 2021/7/6 0006 - 1 7:41
 * 需要进行参数化
 * 请求输入参数  期望返回值
 */
public class LoginTest extends BaseCase {
    //private static final String BASEURL = "http://api.lemonban.com/futureloan";
    //private String loginUrl;
    private List<CaseInfo> cases;
    @BeforeClass
    public void setUpTest(){
        //1.参数初始化
        cases = getCaseDatasFromExcel(1);
        //对用例进行参数化
        cases = paramsReplace(cases);
    }

    @Test(dataProvider="getCaseDatas")
    public void testLogin01(CaseInfo caseInfo) throws JsonProcessingException {
        //loginUrl = BASEURL + caseInfo.getUrl();

        //2.提取出请求体 请求体,发送请求
        //使用jackson将json格式的数据转换为map数据类型
        //ObjectMapper
        Map requestHeaders = fromJsonToMap(caseInfo.getRequestHeader());
        //从excel中读取期望值

        //配置日志
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
        //断言
       assertExpected(caseInfo, response);

        //参数化
        //将token存储到环境变量中
        if(caseInfo.getCaseId() == 1){
            //admin
            GlobalEnvironmentVar.environmentVars.put("admin_token",response.path("data.token_info.token"));
        }else if(caseInfo.getCaseId() == 2){
            //invester
            GlobalEnvironmentVar.environmentVars.put("invester_token",response.path("data.token_info.token"));
        }else if(caseInfo.getCaseId() == 3){
            //loaner
            GlobalEnvironmentVar.environmentVars.put("loaner_token",response.path("data.token_info.token"));
        }
    }

    @DataProvider()
    public Object[] getCaseDatas(){
        return cases.toArray();
    }

}
