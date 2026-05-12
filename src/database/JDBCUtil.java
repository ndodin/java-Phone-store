package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
    public static Connection getConnection() {
        Connection c = null;
        try {
            // Nạp Driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Thông số kết nối của bạn
            String url = "jdbc:mysql://localhost:3306/chdienthoai";
            String user = "root";
            String pass = "@Nghia162006";
            
            c = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException e) {
            System.err.println("Không tìm thấy Driver MySQL! Hãy kiểm tra file JAR.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Lỗi kết nối Database! Kiểm tra URL/User/Pass.");
            e.printStackTrace();
        }
        return c;
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}