package com.example.fitness_club_management_system;

import JDBC.Druid;
import java.sql.Connection;

/**
 * 运行类，用于初始化数据库连接和启动学生信息管理系统
 */
public class run {
    /**
     * 主方法，程序入口
     * @param args 命令行参数
     * @throws Exception 可能抛出的异常
     */
    public static void main(String[] args) throws Exception {
        // 获取数据库连接
        Connection connection = Druid.getConnection();
        // 启动学生信息管理系统
        Main.main(args);
    }
}