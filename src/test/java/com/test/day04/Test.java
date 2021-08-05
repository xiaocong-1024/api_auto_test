package com.test.day04;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xiaocong
 * @date 2021/7/9 0009 - 19:46
 * 参数化 替换的功能实现
 */
public class Test {

    public static void main(String[] args){
        //提取member_id
        //1,将{{member_id}}替换为 值(1111)
        //String content = "/member/{{member_id}}/info";
        //String regStr = "\\{\\{[a-zA-Z_]+\\}\\}";
        //2.将json串中的{{member_id}}替换为 某个具体的值(1111)
        //String content ="{\"code\":\"0\",\"msg\":\"OK\"," +
        //        "\"data.id\":{{member_id}}," +
        //        "\"data.mobile_phone\":\"13333000004\"}";
        //String content = "{\"code\":\"0\",\"msg\":\"OK\",\"data.id\":\"122344\",\"data.mobile_phone\":\"13333000004\"}";
        //String content = "{\"code\":\"0\",\"msg\":\"{{token}}\",\"data.id\":\"{{member_id}}\",\"data.mobile_phone\":\"13333000004\"}";
        String content = "{\"mobile_phone\":\"{{mobile_phone}}\",\"pwd\": \"{{pwd}}\"}";
        Map envirVar = new HashMap<String, Object>();
        envirVar.put("mobile_phone","133330303");
        envirVar.put("pwd","123456");
        String regStr = "\\{\\{([a-zA-Z_]+)\\}\\}";
        Pattern pattern = Pattern.compile(regStr);
        Matcher matcher = pattern.matcher(content);
        String group1 = "";
        while(matcher.find()){
            String group0 = matcher.group(0);
            System.out.println("group0::"+group0);

            group1 = matcher.group(1);
            System.out.println("group1::"+group1);
            //content = matcher.replaceAll(envirVar.get(group1)+"");
            content = content.replace(group0,envirVar.get(group1)+"");
        }
        System.out.println("替换后的内容:"+content);
    }
}
