package com.test.day05;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.testng.Assert;
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

/**
 * @author xiaocong
 * @date 2021/7/6 0006 - 1 7:41
 */
public class GetUserInfoTestV2 {
    private static final String BASEURL = "http://api.lemonban.com/futureloan";
    private String getUserInfoUrl;
    private List<CaseInfo> cases;
    @BeforeTest
    public void setUpTest(){
        //1.参数初始化
        cases = getCaseDatasFromExcel();
    }

    @Test(dataProvider="getCaseDatas")
    public void testGetUserInfo(CaseInfo caseInfo) throws JsonProcessingException {
        //需要对url和expectedResult进行参数化
        getUserInfoUrl = BASEURL + regexReplace(caseInfo.getUrl());

        //2.提取出请求体 请求体,发送请求
        //使用jackson将json格式的数据转换为map数据类型
        //ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        Map requestHeaders = objectMapper.readValue(caseInfo.getRequestHeader(), Map.class);
        //从excel中读取期望值
        //将json格式数据转为map
        String expectedResult = regexReplace(caseInfo.getExpectedResult());
        Map expectedMap = objectMapper.readValue( expectedResult, Map.class);
        Set<Map.Entry<String,Object>> entrys = expectedMap.entrySet();
        //获得期望值(excel期望值中的key是响应体相应字段的Gpath表达式,value是Gpath表达式的值)
        Response response=
        given().
                headers(requestHeaders).
        when().
                get(getUserInfoUrl).
        then().
                log().body().
                extract().response();

        //3.结果断言
        for(Map.Entry<String,Object> entry:entrys){
            Assert.assertEquals(response.path(entry.getKey()).toString(), entry.getValue().toString());
        }
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
            //System.out.println(matcher.group(1));
            envirKey = matcher.group(1);
        }
        //由匹配到的key值去环境变量environmentVar中去取相应的值
        //key --->matcher.group(1)
        Object replacement = GlobalEnvironmentVarV2.environmentVars.get(envirKey);
        return matcher.replaceAll(replacement+"");
    }


    @DataProvider()
    public Object[] getCaseDatas(){
        return cases.toArray();
    }


    public List<CaseInfo> getCaseDatasFromExcel(){
        File excelFile = new File("src/test/resources/api_testcases_futureloan_v1.xls");
        ImportParams importParas = new ImportParams(); //设置excel的配置信息
        //设置读取excel开始的sheet索引
        importParas.setStartSheetIndex(2);
        //设置读取excel的sheet数量
        importParas.setSheetNum(1);
        List<CaseInfo> cases = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, importParas);
        return cases;
    }


}
