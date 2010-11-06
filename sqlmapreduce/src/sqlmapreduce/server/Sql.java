package sqlmapreduce.server;

import com.google.appengine.api.rdbms.AppEngineDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sql {
  static final String RDBMS_CONNECT_STRING = "jdbc:google:rdbms://localhost:1234#googlecom:fredsa";

  private static final Logger log = Logger.getLogger(Sql.class.getName());

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
      return DriverManager.getConnection(Sql.RDBMS_CONNECT_STRING, "sa", null);
    } catch (SQLException e) {
      log.log(Level.SEVERE, "failed to open database connection", e);
      throw new RuntimeException(e);
    }
  }

}
