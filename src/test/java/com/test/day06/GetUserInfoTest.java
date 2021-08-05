package com.test.day06;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.day06.CaseInfo;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.unregisterParser;

/**
 * @author xiaocong
 * @date 2021/7/6 0006 - 1 7:41
 * 参数化封装 --->paramsReplace
 * 对excel中的请求头  接口地址  请求参数  期望返回结果做参数化
 */
public class GetUserInfoTest {
    private static final String BASEURL = "http://api.lemonban.com/futureloan";
    private String getUserInfoUrl;
    private List<CaseInfo> cases;
    @BeforeClass
    public void setUpTest(){
        //1.参数初始化
        //cases --->从excel中取出的原始的case
        cases = getCaseDatasFromExcel();
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

    //参数化
    //excel中需要做参数化的列有
    //请求头  接口地址   参数输入  期望返回结果
    public List<CaseInfo> paramsReplace(List<CaseInfo> caseInfos){
        for(CaseInfo caseInfo:caseInfos){
            if(caseInfo.getRequestHeader()!=null){
                String requestHeader = regexReplace(caseInfo.getRequestHeader());
                caseInfo.setRequestHeader(requestHeader);
            }
            if(caseInfo.getUrl()!=null){
                String url = regexReplace(caseInfo.getUrl());
                caseInfo.setUrl(url);
            }
            if(caseInfo.getInputParams()!=null){
                String inputParams = regexReplace(caseInfo.getInputParams());
                caseInfo.setInputParams(inputParams);
            }

            if(caseInfo.getExpectedResult()!=null){
                String expected = regexReplace(caseInfo.getExpectedResult());
                caseInfo.setExpectedResult(expected);
            }
        }
        return caseInfos;
    }


    /**
    * @Param  srcContent:原始的字符串
    * @Return  替换后的字符串
    *
    */
    public static String regexReplace(String srcContent){
        String regStr = "\\{\\{([a-zA-Z_]+)\\}\\}";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(srcContent);
        String envirKey = "";
        while(matcher.find()){
            //System.out.println(matcher.group(0));
            System.out.println(matcher.group(1));
            envirKey = matcher.group(1);
        }
        //由匹配到的key值去环境变量environmentVar中去取相应的值
        //key --->matcher.group(1)
        Object replacement = GlobalEnvironmentVar.environmentVars.get(envirKey);
        return matcher.replaceAll(replacement+"");
    }


    @DataProvider()
    public Object[] getCaseDatas(){
        return cases.toArray();
    }


    public List<CaseInfo> getCaseDatasFromExcel(){
        File excelFile = new File("src/test/resources/api_testcases_futureloan_v2.xls");
        ImportParams importParas = new ImportParams(); //设置excel的配置信息
        //设置读取excel开始的sheet索引
        importParas.setStartSheetIndex(2);
        //设置读取excel的sheet数量
        importParas.setSheetNum(1);
        List<CaseInfo> cases = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, importParas);
        return cases;
    }


}
