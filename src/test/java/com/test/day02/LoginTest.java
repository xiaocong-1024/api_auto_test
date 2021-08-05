package com.test.day02;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Import;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * @author xiaocong
 * @date 2021/7/6 0006 - 17:41
 */
public class LoginTest {
    private static final String BASEURL = "http://api.lemonban.com/futureloan";
    private String loginUrl;

    @Test(dataProvider="getCaseDatas")
    public void testLogin01(CaseInfo caseInfo) throws JsonProcessingException {
        loginUrl = BASEURL + caseInfo.getUrl();

        //使用jackson将json格式的数据转换为map数据类型
        //ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(caseInfo.getRequestHeader(), Map.class);

        given().
                headers(map).
                body(caseInfo.getInputParams()).
        when().
                post(loginUrl).
        then().
                log().body();
    }

    @DataProvider()
    public Object[] getCaseDatas(){
        File excelFile = new File("src/test/resources/api_testcases_futureloan_v1.xls");

        ImportParams importParas = new ImportParams(); //设置excel的配置信息
        //设置读取excel开始的sheet索引
        importParas.setStartSheetIndex(1);
        //设置读取excel的sheet数量
        importParas.setSheetNum(1);
        List<CaseInfo> cases = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, importParas);
        return cases.toArray();
    }


    //public static void main(String[] args){
    //    //读取excel中的数据
    //    //EasyPOI技术
    //    File excelFile = new File("src/test/resources/api_testcases_futureloan_v1.xls");
    //
    //    ImportParams importParas = new ImportParams(); //设置excel的配置信息
    //    //设置读取excel开始的sheet索引
    //    importParas.setStartSheetIndex(1);
    //    //设置读取excel的sheet数量
    //    importParas.setSheetNum(1);
    //    List<CaseInfo> cases = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, importParas);
    //    for(CaseInfo caseInfo:cases){
    //        System.out.println(caseInfo);
    //    }
    //}

}
