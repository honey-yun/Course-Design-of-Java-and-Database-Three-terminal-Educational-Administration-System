package server;

import java.sql.*;

public class DBUtil {
    private static final String URL = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=keshe";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "123456";

    static {
        try {
            // 加载数据库驱动
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // 获取数据库连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    // 关闭数据库资源（连接、语句、结果集）
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