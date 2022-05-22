package vocabulary.bot.dao.persistence;

import vocabulary.bot.tools.ResourcesAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

  private final static String DB_URL = ResourcesAccess.getFromResources("db_url");
  private final static String DB_USER = ResourcesAccess.getFromResources("db_user");
  private final static String DB_PASS = ResourcesAccess.getFromResources("db_password");

  private static ConnectionManager instance;
  private Connection connection;
  {
    try {
      connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private ConnectionManager() {
    try {
      Class.forName("org.postgresql.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println("Unable to find db driver: " + e.getMessage());
    }
  }

  public static ConnectionManager getInstance() {
    if (instance == null) {
      instance = new ConnectionManager();
    }
    return instance;
  }

  public Connection getConnection(){
    return connection;
  }

}
