/***********************************************************
 * 版权所有 (C)2024, Lishiyun
 *
 * 文件名称： DBUtil.java
 * 文件标识：无
 * 内容摘要：用于数据库基本操作
 * 其它说明：无
 * 当前版本： V1.0
 * 作   者：李世赟
 * 完成日期： 20241130
 **********************************************************/
package server;

import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=keshe";
    private static final String USERNAME = "sa";//账号
    private static final String PASSWORD = "123456";//密码

    static {
        try {
            // 加载数据库驱动
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /*********************************************************
     * 功能描述：获取数据库连接对象
     * 输入参数：无
     * 返回值：Connection - 成功则返回可用的数据库连接对象，若出现 SQL 异常或类加载异常则抛出异常给调用者
     * 其它说明：无
     ************************************************************/
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
    /*********************************************************
     * 功能描述：关闭数据库相关资源，包括连接、语句和结果集
     * 输入参数：
     * - conn：数据库连接对象，可为 null，表示无需关闭连接对象
     * - stmt：SQL 语句执行对象，可为 null，表示无需关闭语句对象
     * - rs：查询结果集对象，可为 null，表示无需关闭结果集对象
     * 返回值：无
     * 其它说明：无
     ************************************************************/
    public static void closeResources(Connection conn, PreparedStatement stmt, ResultSet rs) {
        if (rs!= null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt!= null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn!= null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}