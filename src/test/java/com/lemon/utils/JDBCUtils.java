package com.lemon.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author xiaocong
 * @date 2021/7/13 0013 - 19:55
 * 定义一个JDBC的工具类
 * 返回一个连接对象
 */
public class JDBCUtils {
    public static Connection getConnection(){
    //    定义数据库连接
    //    oracel:jdbc:oracel:thin:@localhost:1521:DBname
        //sqlserver:jdbc:microsoft:sqlserver://localhost:1433;DatabaseName=DBname
        //mysql:jdbc:mysql://localhost:3306/DBname
        String url = "jdbc:mysql://8.129.91.152:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
        String user = "future";
        String password = "123456";
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return connection;
    }

    /**
    * @Param  sql语句
    * @Return 查询结果多条记录
     *
    */
    public static List<Map<String,Object>> queryAll(String sqlStr){
        Connection conn = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            List<Map<String, Object>> result = queryRunner.query(conn,sqlStr,new MapListHandler());
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //关闭连接
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
    * @Param  sql查询语句
    * @Return  查询结果单条记录
    *
    */
    public static Map<String,Object> queryOne(String sqlStr){
        Connection conn = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            Map<String, Object> result = queryRunner.query(conn,sqlStr,new MapHandler());
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //关闭连接
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
    * @Param  查询语句
    * @Return  单个记录
    *
    */

    public static Object querySingle(String sqlStr){
        Connection conn = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            Object result = queryRunner.query(conn,sqlStr,new ScalarHandler<>());
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //关闭连接
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }


    public static void update(String sqlStr){
        Connection conn = getConnection();
        QueryRunner queryRunner = new QueryRunner();
        try {
            queryRunner.update(conn,sqlStr);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //关闭连接
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
