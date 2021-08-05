package com.lemon.base;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lemon.data.Constants;
import com.lemon.pojo.CaseInfo;
import com.lemon.data.GlobalEnvironmentVar;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaocong
 * @date 2021/7/13 0013 - 11:22
 * 测试类的共性提取
 */
public class BaseCase {

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
            System.out.println("group0::"+group0);

            group1 = matcher.group(1);
            System.out.println("group1::"+group1);
            //content = matcher.replaceAll(envirVar.get(group1)+"");
            srcContent = srcContent.replace(group0,GlobalEnvironmentVar.environmentVars.get(group1)+"");
        }
        return srcContent;
    }


}
