package sample;
import java.sql.*;
import java.sql.Connection;

public class DbConnection {
    public static Connection connector() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:TestDB.db");
            return conn;
        } catch (Exception e) {
            return null;
        }
    }
}
