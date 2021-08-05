package com.kingsoft.testcases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingsoft.base.BaseCase;
import com.kingsoft.data.GlobalEnvironmentVar;
import com.kingsoft.pojo.CaseInfo;
import com.kingsoft.utils.PhoneRandom;
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
 * @date 2021/7/13 0013 - 11:30
 * 注册接口不需要参数化
 * 只要前三个case需要进行参数化(由caseid判断然后进行参数化即可)
 */
public class RegisterTest extends BaseCase {
    //private static final String BASEURL = "http://api.lemonban.com/futureloan";
    //private String loginUrl;
    private List<CaseInfo> cases;

    @BeforeClass
    public void setUpClass(){
        //从excel中读取case
        cases = getCaseDatasFromExcel(0);
    }


    @Test(dataProvider = "getRegisterCases")
    public void testRegister(CaseInfo caseInfo) throws JsonProcessingException {
        //前三条case需要进行参数化
        if(caseInfo.getCaseId() == 1){
            String  adminMobilePhone = PhoneRandom.getNoRegisterPhone();
            GlobalEnvironmentVar.environmentVars.put("admin_mobile_phone",adminMobilePhone);
        }else if(caseInfo.getCaseId() == 2){
            String  investerMobilePhone = PhoneRandom.getNoRegisterPhone();
            GlobalEnvironmentVar.environmentVars.put("invester_mobile_phone",investerMobilePhone);
        }else if(caseInfo.getCaseId() == 3){
            String  loanerMobilePhone = PhoneRandom.getNoRegisterPhone();
            GlobalEnvironmentVar.environmentVars.put("loaner_mobile_phone",loanerMobilePhone);
        }
        //进行参数化
        caseInfo = paramsReplaceCaseInfo(caseInfo);
        //2.提取出请求体 请求体,发送请求
        //使用jackson将json格式的数据转换为map数据类型
        //ObjectMapper
        Map requestHeaders = fromJsonToMap(caseInfo.getRequestHeader());
        String inputParams = caseInfo.getInputParams();

        //发送请求前：配置日志
        String logFilePath = addLogToFile(caseInfo.getInterfaceName(), caseInfo.getCaseId());
        Response response=
                given().
                        log().all().
                        headers(requestHeaders).
                        body(inputParams).
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

        //3.接口响应结果断言
        assertExpected(caseInfo, response);

        //4.数据库断言
        assertSQL(caseInfo);

        //参数化（将变量值写入环境变量中）
        Map inputParamsMap = fromJsonToMap(inputParams);
        if(caseInfo.getCaseId() == 1){
            //admin
            GlobalEnvironmentVar.environmentVars.put("admin_pwd",inputParamsMap.get("pwd"));
            GlobalEnvironmentVar.environmentVars.put("admin_member_id",response.path("data.id"));
        }else if(caseInfo.getCaseId() == 2){
            //invester
            GlobalEnvironmentVar.environmentVars.put("invester_pwd",inputParamsMap.get("pwd"));
            GlobalEnvironmentVar.environmentVars.put("invester_member_id",response.path("data.id"));
        }else if(caseInfo.getCaseId() == 3){
            //loaner
            GlobalEnvironmentVar.environmentVars.put("loaner_pwd",inputParamsMap.get("pwd"));
            GlobalEnvironmentVar.environmentVars.put("loaner_member_id",response.path("data.id"));
        }
    }

    @DataProvider
    public Object[] getRegisterCases(){
        return cases.toArray();
    }

}
