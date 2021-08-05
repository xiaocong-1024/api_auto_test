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

import static io.restassured.RestAssured.given;

/**
 * @author xiaocong
 * @date 2021/7/6 0006 - 1 7:41
 * 需要进行参数化
 * 请求输入参数  期望返回值
 */
public class LoginTest extends BaseCase {
    private static final String BASEURL = "http://api.lemonban.com/futureloan";
    private String loginUrl;
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
        loginUrl = BASEURL + caseInfo.getUrl();

        //2.提取出请求体 请求体,发送请求
        //使用jackson将json格式的数据转换为map数据类型
        //ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        Map requestHeaders = objectMapper.readValue(caseInfo.getRequestHeader(), Map.class);
        //从excel中读取期望值
        //将json格式数据转为map
        Map expectedMap = objectMapper.readValue( caseInfo.getExpectedResult(), Map.class);
        Set<Map.Entry<String,Object>> entrys = expectedMap.entrySet();
        //获得期望值(excel期望值中的key是响应体相应字段的Gpath表达式,value是Gpath表达式的值)
        Response response=
        given().
                headers(requestHeaders).
                body(caseInfo.getInputParams()).
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
        String token = response.path("data.token_info.token");
        ////将响应结果中的id保存到环境变量中
        if(id != null){
        //    GlobalEnvironmentVarV1.memberId = id;
            GlobalEnvironmentVar.environmentVars.put("member_id",id);
            //需要将token值写入环境变量中
            GlobalEnvironmentVar.environmentVars.put("token",token);
        }
    }

    @DataProvider()
    public Object[] getCaseDatas(){
        return cases.toArray();
    }

}
