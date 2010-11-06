package sqlmapreduce.shared;

import com.google.appengine.api.rdbms.AppEngineDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Constants {
  public static final String INITIAL_SQL = "select * from contact;\nselect * from employee;";

  public static final String KIND = "contact";

  static final String RDBMS_CONNECT_STRING = "jdbc:google:rdbms://localhost:1234#googlecom:fredsa";

  private static final Logger log = Logger.getLogger(Constants.class.getName());
  static {
    try {
      DriverManager.registerDriver(new AppEngineDriver());
    } catch (SQLException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  public static void closeConnection(Connection connection) {
    try {
      connection.close();
    } catch (SQLException e) {
      log.log(Level.SEVERE, "failed to close database connection", e);
    }
  }

  public static Connection getConnection() {
    try {
      Connection c = DriverManager.getConnection(Constants.RDBMS_CONNECT_STRING, "sa", null);

      try {
        Statement stmt = c.createStatement();
        stmt.executeUpdate("create database fred");
      } catch (Exception ignore) {
      }

      try {
        c.setCatalog("fredsa");
      } catch (Exception ignore) {
      }

      return c;
    } catch (SQLException e) {
      log.log(Level.SEVERE, "failed to open database connection", e);
      throw new RuntimeException(e);
    }
  }

}
