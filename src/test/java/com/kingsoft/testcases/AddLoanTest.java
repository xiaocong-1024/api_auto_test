package com.kingsoft.testcases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingsoft.base.BaseCase;
import com.kingsoft.data.GlobalEnvironmentVar;
import com.kingsoft.pojo.CaseInfo;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @author xiaocong
 * @date 2021/7/15 0015 - 16:52
 * 添加项目(loaner)
 *
 */
public class AddLoanTest extends BaseCase {
    //
    private List<CaseInfo> cases;
    @BeforeClass
    public void setUpTest(){
        //1.参数初始化
        cases = getCaseDatasFromExcel(4);
        //对用例进行参数化
        cases = paramsReplace(cases);
    }

    /**
    * @Param  一条测试用例
    * @Return 空
    * 项目创建成功后，需要将响应里的项目id存储至环境变量中--->后续审核项目接口需要
    */

    @Test(dataProvider="getCaseDatas")
    public void testAddLoan(CaseInfo caseInfo) throws JsonProcessingException {
        //配置日志
        String logFilePath = addLogToFile(caseInfo.getInterfaceName(), caseInfo.getCaseId());
        Map requestHeaders = fromJsonToMap(caseInfo.getRequestHeader());
        //从excel中读取期望值
        //rest-assured默认会将返回结果json里面的小数通过Float来存储
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
        assertSQL(caseInfo);
        //将loan_id存储起来(caseid 1 -4 都可以成功加标)
        Integer loan_id = response.path("data.id");
        if(caseInfo.getCaseId() == 1){
            //购买测试课程_1
            GlobalEnvironmentVar.environmentVars.put("loan_id1", loan_id);
        }else if(caseInfo.getCaseId() == 2){
            //购买测试课程_2
            GlobalEnvironmentVar.environmentVars.put("loan_id2", loan_id);
        }else if(caseInfo.getCaseId() == 3){
            //购买测试课程_3
            GlobalEnvironmentVar.environmentVars.put("loan_id3", loan_id);
        }else if(caseInfo.getCaseId() == 4){
            //购买测试课程_4
            GlobalEnvironmentVar.environmentVars.put("loan_id4", loan_id);
        }

    }

    @DataProvider()
    public Object[] getCaseDatas(){
        return cases.toArray();
    }
}
