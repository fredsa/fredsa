package sqlmapreduce.server;

import com.google.appengine.api.rdbms.AppEngineDriver;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import sqlmapreduce.client.SqlService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SqlServiceImpl extends RemoteServiceServlet implements SqlService {

  static {
    try {
      DriverManager.registerDriver(new AppEngineDriver());
    } catch (SQLException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  public String greetServer(String input) {
    try {
      return query(input);
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  private String query(String sql) throws SQLException {
    String t = "";
    Connection c = DriverManager.getConnection(
        "jdbc:google:speckle://localhost:1234#googlecom:fredsa", "sa", null);

    try {
      c.createStatement().executeUpdate("create database fred;");
    } catch (Exception ignore) {
    }

    try {
      c.setCatalog("fredsa");
    } catch (Exception ignore) {
    }

    try {
      c.createStatement().executeUpdate(
          "create table message(line varchar(200), line2 varchar(200))");
    } catch (Exception ignore) {
    }

    try {
      c.createStatement().executeUpdate("insert into message values('Hello World', 'xxx')");
    } catch (Exception ignore) {
    }

    if (sql.trim().toLowerCase().startsWith("select")) {
      ResultSet query = c.createStatement().executeQuery(sql);
      ResultSetMetaData metaData = query.getMetaData();
      while (query.next()) {
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          if (i > 1) {
            t += "\t";
          }
          t += query.getString(i);
        }
        t += "\n";
      }
    } else {
      Statement statement = c.createStatement();
      statement.execute(sql);
      t += "update count = " + statement.getUpdateCount();
    }
    c.close();
    return t;
  }

}
