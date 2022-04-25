package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigDB {
  private static String URL = "jdbc:oracle:thin:@localhost:1521:ORCLCDB";
  private static String USER = "java_app";
  private static String PASSWORD = "java_app";

  public static Connection getConnection() throws SQLException {
    Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
    return connection;
  }
}
