package com.lemon.testcases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lemon.base.BaseCase;
import com.lemon.data.GlobalEnvironmentVar;
import com.lemon.pojo.CaseInfo;
import com.lemon.utils.PhoneRandom;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
    private static final String BASEURL = "http://api.lemonban.com/futureloan";
    private String loginUrl;
    private List<CaseInfo> cases;

    @BeforeClass
    public void setUpClass(){
        //从excel中读取case
        cases = getCaseDatasFromExcel(0);
        //对case进行参数化
        //setEnvironmentVar();
        //cases = paramsReplace(cases);
    }


    //public void setEnvironmentVar(){
    //    //先将参数化的数据写入环境变量中
    //    String mobile_phone1 = PhoneRandom.getNoRegisterPhone();
    //    GlobalEnvironmentVar.environmentVars.put("mobile_phone1",mobile_phone1);
    //    String mobile_phone2 = "";
    //    String mobile_phone3 = "";
    //    while(true){
    //        mobile_phone2 = PhoneRandom.getNoRegisterPhone();
    //        if(!mobile_phone2.equals(mobile_phone1)){
    //            GlobalEnvironmentVar.environmentVars.put("mobile_phone2",mobile_phone2);
    //            break;
    //        }
    //    }
    //    //获取一个随机mobile_phone3
    //    while(true){
    //        mobile_phone3 = PhoneRandom.getNoRegisterPhone();
    //        if(mobile_phone3.equals(mobile_phone1) || mobile_phone3.equals(mobile_phone2)){
    //            continue;
    //        }else{
    //            GlobalEnvironmentVar.environmentVars.put("mobile_phone3",mobile_phone3);
    //            break;
    //        }
    //    }
    //}


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
        loginUrl = BASEURL + caseInfo.getUrl();
        //2.提取出请求体 请求体,发送请求
        //使用jackson将json格式的数据转换为map数据类型
        //ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        Map requestHeaders = objectMapper.readValue(caseInfo.getRequestHeader(), Map.class);

        String inputParams = caseInfo.getInputParams();

        //从excel中读取期望值
        //将json格式数据转为map
        Map expectedMap = objectMapper.readValue( caseInfo.getExpectedResult(), Map.class);
        Set<Map.Entry<String,Object>> entrys = expectedMap.entrySet();
        //获得期望值(excel期望值中的key是响应体相应字段的Gpath表达式,value是Gpath表达式的值)
        Response response=
                given().
                        headers(requestHeaders).
                        body(inputParams).
                when().
                        post(loginUrl).
                then().
                        log().body().
                        extract().response();

        //3.结果断言
        for(Map.Entry<String,Object> entry:entrys){
            Assert.assertEquals(response.path(entry.getKey()).toString(), entry.getValue().toString());
        }

        Integer id = response.path("data.id");
        ////将响应结果中的id保存到环境变量中
        //将mobile_phone  pwd保存至环境变量
        if(id != null){
            Object mobilephone = response.path("data.mobile_phone");
            GlobalEnvironmentVar.environmentVars.put("mobile_phone",mobilephone+"");
            //需要将请求输入参数中的pwd写入环境变量中
            ObjectMapper objectMapper1 = new ObjectMapper();
            Map inputParamsMap = objectMapper1.readValue(inputParams, Map.class);
            Object pwd = inputParamsMap.get("pwd");
            GlobalEnvironmentVar.environmentVars.put("pwd",pwd);
        }
    }

    @DataProvider
    public Object[] getRegisterCases(){
        return cases.toArray();
    }

}
