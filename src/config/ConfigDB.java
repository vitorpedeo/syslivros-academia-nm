package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConfigDB {
  private static String URL = "jdbc:postgresql://localhost:5432/syslivros";
  private static String USER = "syslivros";
  private static String PASSWORD = "syslivros";

  public static Connection getConnection() throws SQLException {
    Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
    return connection;
  }
}
