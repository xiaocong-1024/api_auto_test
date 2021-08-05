package com.test.day03;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author xiaocong
 * @date 2021/7/6 0006 - 17:55
 */
//EXCEL对象的映射类
public class CaseInfo {
    @Excel(name="序号(caseId)")
    private Integer caseId;

    @Excel(name="接口模块(interface)")
    private String interfaceName;

    @Excel(name="用例标题(title)")
    private String title;

    @Excel(name="请求头(requestHeader)")
    private String requestHeader;

    @Excel(name="请求方式(method)")
    private String method;

    @Excel(name="接口地址(url)")
    private String url;

    @Excel(name="参数输入(inputParams)")
    private String inputParams;

    @Excel(name="期望返回结果(expected)")
    private String expectedResult;

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInputParams() {
        return inputParams;
    }

    public void setInputParams(String inputParams) {
        this.inputParams = inputParams;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    @Override
    public String toString() {
        return "CaseInfo{" +
                "caseId=" + caseId +
                ", interfaceName='" + interfaceName + '\'' +
                ", title='" + title + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", inputParams='" + inputParams + '\'' +
                ", expectedResult='" + expectedResult + '\'' +
                '}';
    }
}
