package com.test.day06;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaocong
 * @date 2021/7/9 0009 - 20:53
 * 设置环境变量
 * 优化:使用map才存储环境变量
 */
public class GlobalEnvironmentVar {
    //public static Integer memberId=0;
    public static Map<String,Object> environmentVars=new HashMap<>();
}
