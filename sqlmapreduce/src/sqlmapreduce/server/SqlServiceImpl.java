package sqlmapreduce.server;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import sqlmapreduce.client.SqlService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class SqlServiceImpl extends RemoteServiceServlet implements SqlService {

  public String greetServer(String input) {
    try {
      return query(input);
    } catch (SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }

  public String initDatabase() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    String[] FIRST_NAMES = {"Ford", "Arthur", "Zaphod", "Tricia"};
    String[] LAST_NAMES = {"Prefect", "Dent", "Beeblebrox", "McMillan"};

    String t = "";
    for (String first : FIRST_NAMES) {
      for (String last : LAST_NAMES) {
        String kind = "Contact";
        Entity entity = new Entity(kind);
        entity.setProperty("first_name", first);
        entity.setProperty("last_name", last);
        if (t.length() > 0) {
          t += "<br>";
        }
        t += "Kind=" + kind + "(first_name=" + first + ", last_name=" + last + ")";
        ds.put(entity);
      }
    }
    return t;
  }

  private String query(String sql) throws SQLException {
    String t = "";
    Connection c = Sql.getConnection();

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
