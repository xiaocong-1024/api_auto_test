package com.kingsoft.base;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kingsoft.data.Constants;
import com.kingsoft.data.GlobalEnvironmentVar;
import com.kingsoft.pojo.CaseInfo;
import com.lemon.utils.JDBCUtils;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @author xiaocong
 * @date 2021/7/13 0013 - 11:22
 * 测试类的共性提取
 */
public class BaseCase {
    //在所有的测试用例执行之前执行
    @BeforeTest
    public void globalSetUp(){
        //全局配置
        //1.一次配置，全局生效
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
         //2.加入下面的config配置，rest-assured响应结果中的小数就会通过BigDecimal来存储（默认情况下，会使用Float进行存储）
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //3.全局日志配置(所有的case都集成到一个日志文件)
        //通过REST-Assured过滤器实现
        //try {
        //    PrintStream fileOutStream = new PrintStream(new File("log/test.log"));
        //    RestAssured.filters(new RequestLoggingFilter(fileOutStream), new ResponseLoggingFilter(fileOutStream));
        //} catch (FileNotFoundException e) {
        //    e.printStackTrace();
        //}
    }

    /**
    * @Param  interfaceName：接口名称 id：测试id
    * @Return  日志文件路径
    *
    */
    
    public String addLogToFile(String interfaceName,Integer id){
        //创建日志目录
        String logFilePath = "";
        if(!Constants.IS_DEBUG) {
            String logDirPath = "target/log/" + interfaceName;
            File logDirFile = new File(logDirPath);
            if (!logDirFile.exists()) {
                //会创建目录及目录下的子目录
                logDirFile.mkdirs();
            }
            logFilePath = logDirPath + "/" + interfaceName + "_" + id + ".log";
            //添加日志的配置信息
            try {
                //日志文件的输出流
                PrintStream fileOutPutStream = new PrintStream(new File(logFilePath));
                RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return logFilePath;
    }


    //从excel中读取数据
    public List<CaseInfo> getCaseDatasFromExcel(int index){
        File excelFile = new File(Constants.excelPath);
        ImportParams importParas = new ImportParams(); //设置excel的配置信息
        //设置读取excel开始的sheet索引
        importParas.setStartSheetIndex(index);
        //设置读取excel的sheet数量
        importParas.setSheetNum(1);
        List<CaseInfo> cases = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, importParas);
        return cases;
    }

   /**
   * @Param  一条case
   * @Return  参数化替换后的结果
   * 对一条case进行参数
   */

    public CaseInfo paramsReplaceCaseInfo(CaseInfo caseInfo){
        String requestHeader = regexReplace(caseInfo.getRequestHeader());
        caseInfo.setRequestHeader(requestHeader);
        String url = regexReplace(caseInfo.getUrl());
        caseInfo.setUrl(url);
        String inputParams = regexReplace(caseInfo.getInputParams());
        caseInfo.setInputParams(inputParams);
        String expected = regexReplace(caseInfo.getExpectedResult());
        caseInfo.setExpectedResult(expected);
        String checkSQL = regexReplace(caseInfo.getCheckSQL());
        caseInfo.setCheckSQL(checkSQL);
        return caseInfo;
    }


    //测试用例参数化
    //参数化
    //excel中需要做参数化的列有
    //请求头  接口地址   参数输入  期望返回结果
    public List<CaseInfo> paramsReplace(List<CaseInfo> caseInfos){
        for(CaseInfo caseInfo:caseInfos){
                String requestHeader = regexReplace(caseInfo.getRequestHeader());
                caseInfo.setRequestHeader(requestHeader);
                String url = regexReplace(caseInfo.getUrl());
                caseInfo.setUrl(url);
                String inputParams = regexReplace(caseInfo.getInputParams());
                caseInfo.setInputParams(inputParams);
                String expected = regexReplace(caseInfo.getExpectedResult());
                caseInfo.setExpectedResult(expected);
                String checkSQL = regexReplace(caseInfo.getCheckSQL());
                caseInfo.setCheckSQL(checkSQL);
        }
        return caseInfos;
    }


    /**
     * @Param  srcContent:原始的字符串
     * @Return  替换后的字符串
     *
     */
    public static String regexReplace(String srcContent){
        //如果srcContent为null,则不需要进行参数化替换
        if(srcContent == null){
            return srcContent;
        }
        String regStr = "\\{\\{([a-zA-Z0-9_]+)\\}\\}";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(srcContent);
        String group1 = "";
        while(matcher.find()){
            String group0 = matcher.group(0);
            //System.out.println("group0::"+group0);

            group1 = matcher.group(1);
            //System.out.println("group1::"+group1);
            //content = matcher.replaceAll(envirVar.get(group1)+"");
            srcContent = srcContent.replace(group0,GlobalEnvironmentVar.environmentVars.get(group1)+"");
        }
        return srcContent;
    }

    /**
    * @Param  caseInfo:一条测试用例  res：响应体
    * @Return 空
    * 断言接口响应结果
    */

    public void assertExpected(CaseInfo caseInfo, Response res){
        //如果期望值里面有小数,则需要将该期望值转为BigDecimal
        ObjectMapper objectMapper = new ObjectMapper();
        Map expectedMap = null;
        try {
            expectedMap = objectMapper.readValue( caseInfo.getExpectedResult(), Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        Set<Map.Entry<String,Object>> entrys = expectedMap.entrySet();
        //获得期望值(excel期望值中的key是响应体相应字段的Gpath表达式,value是Gpath表达式的值)
        for(Map.Entry<String,Object> entry:entrys){
            Object expected = entry.getValue();
            //System.out.println("期望值的类型::"+expected.getClass());
            if(expected instanceof Float || expected instanceof Double){
                //如果期望值是小数(Double/Float),则将其转为BigDecimal
                BigDecimal expectedBigDecimal = new BigDecimal(expected.toString());
                Assert.assertEquals(res.path(entry.getKey()),expectedBigDecimal,"接口响应结果断言失败！！！");
            }else{
                Assert.assertEquals(res.path(entry.getKey()).toString(), entry.getValue().toString(),"接口响应结果断言失败！！！");
            }
        }
    }
    /**
    * @Param  一条测试case
    * @Return   void
    * 检验数据库(如果用例执行成功，才会去校验数据库)
    */

    public void assertSQL(CaseInfo caseInfo){
        if(caseInfo.getCheckSQL() != null){
            Map checkSQLMap = fromJsonToMap(caseInfo.getCheckSQL());
            Set<Map.Entry<String,Object>> entrySet = checkSQLMap.entrySet();
            for(Map.Entry<String,Object> entry:entrySet){
                String sql = entry.getKey();
                Object actual = JDBCUtils.querySingle(sql);
                //System.out.println("实际值(数据库读取)数据类型::"+actual.getClass());
                //System.out.println("期望值(Excel读取json)数据类型::"+entry.getValue().getClass());
                if(actual instanceof Long) {
                    //实际值(数据库读取)数据类型::class java.lang.Long
                    //期望值(Excel读取json)数据类型::class java.lang.Integer
                    //需要将期望值转为Long
                    Long expected = new Long(entry.getValue().toString());
                    Assert.assertEquals(actual, expected, "数据库断言失败!!");
                }else if(actual instanceof BigDecimal){
                    //实际值(数据库读取)数据类型::class java.math.BigDecimal
                    //期望值(Excel读取json)数据类型::class java.lang.Double
                    //需要将期望值数据类型Double转为BigDecimal
                    BigDecimal expected = new BigDecimal(entry.getValue().toString());
                    Assert.assertEquals(actual, expected, "数据库断言失败!!");
                }else{
                    Assert.assertEquals(actual,entry.getValue(),"数据库断言失败!!");
                }
            }

        }
    }


    /**
    * @Param  json字符串
    * @Return  json字符串转的map
    * 将json字符串转为map
    */

    public Map fromJsonToMap(String jsonStr){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonStr, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
