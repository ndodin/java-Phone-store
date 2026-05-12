package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=CHDienThoai;encrypt=true;trustServerCertificate=true;";
        String user = "sa";
        String pass = "123456"; 
        return DriverManager.getConnection(url, user, pass);
    }
}