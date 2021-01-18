package sample;
import java.sql.*;

public class DbModel {
    Connection connection;

    public DbModel () {
        connection = DbConnection.connector();
        if (connection == null) System.out.println("Geen verbinding");
    }

    public boolean isDbConnected() {
        try {
            return !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
