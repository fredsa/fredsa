package sqlmapreduce.server;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.FetchOptions.Builder;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import sqlmapreduce.client.RpcService;
import sqlmapreduce.shared.Constants;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class RpcServiceImpl extends RemoteServiceServlet implements RpcService {

  private static final int LEN = 15;

  private static final String SELECT_STAR_FROM = "select * from ";

  private DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

  private String t;

  public String executeDatastoreQuery(String namespace, String sql) {
    t = "";
    NamespaceManager.set(namespace);

    String[] queries = sql.split(";");
    for (String query : queries) {
      query = query.trim();
      if (query.length() > 0) {
        try {
          doDatastoreQuery(query);
        } catch (SQLException e) {
          logError(e);
        }
      }
    }
    return t;
  }

  public String executeRelationalQuery(String sql) {
    t = "";
    String[] queries = sql.split(";");
    for (String query : queries) {
      query = query.trim();
      if (query.length() > 0) {
        try {
          doRelationalQuery(query);
        } catch (SQLException e) {
          logError(e);
        }
      }
    }
    return t;
  }

  public String initDatastore(String namespace) {
    t = "";
    NamespaceManager.set(namespace);

    String[] FIRST_NAMES = {"Ford", "Arthur", "Zaphod", "Tricia", null};
    String[] MIDDLE_INITIAL = {"A", "X", null};
    String[] LAST_NAMES = {"Prefect", "Dent", "Beeblebrox", "McMillan", null};

    int count = 0;
    for (String first : FIRST_NAMES) {
      for (String mi : MIDDLE_INITIAL) {
        for (String last : LAST_NAMES) {
          String kind = Constants.KIND;
          Entity entity = new Entity(kind);

          if (first != null) {
            entity.setProperty("first", first);
          }
          if (mi != null) {
            entity.setProperty("mi", mi);
          }
          if (last != null) {
            entity.setProperty("last", last);
          }

          String message = "Kind=" + kind + "(";
          message += "first: " + formatValue(LEN, first);
          message += " mi: " + formatValue(4, mi);
          message += " last: " + formatValue(LEN, last);
          message += ")";
          logResult(message);
          ds.put(entity);
          count++;
        }
      }
    }
    logStatus(count + " entities created");
    return t;
  }

  public String initRelational() {
    t = "";
    Connection c = Util.getConnection();

    executeUpdate(c, "drop table contact;");
    executeUpdate(c, "drop table employee;");
    executeUpdate(c, "create table employee ( id int, name varchar(200) )");
    executeUpdate(c, "insert into employee values(1, 'Patrick Chanezon')");
    executeUpdate(c, "insert into employee values(42, 'Fred Sauer')");

    return t;
  }

  private void doDatastoreQuery(String sql) throws SQLException {
    logQuery(sql);
    sql = sql.replaceAll("\\s+", " ").trim().toLowerCase();
    if (!sql.startsWith(SELECT_STAR_FROM)) {
      logError("Unrecognized GQL query");
      return;
    }
    String kind = sql.substring(SELECT_STAR_FROM.length());
    PreparedQuery prepared = ds.prepare(new Query(kind));
    List<Entity> results = prepared.asList(Builder.withDefaults());
    for (Entity entity : results) {
      Map<String, Object> props = entity.getProperties();
      String propList = "Kind=" + kind + "(";
      for (Entry<String, Object> entry : props.entrySet()) {
        String name = entry.getKey();
        String value = formatValue(LEN, "" + entry.getValue());
        propList += name + ": " + value;
      }
      propList += ")";
      logResult(propList);
    }
    logStatus(results.size() + " results");
  }

  private void doRelationalQuery(String sql) throws SQLException {
    Connection c = Util.getConnection();

    logQuery(sql);
    if (sql.trim().toLowerCase().startsWith("select")) {
      ResultSet query = c.createStatement().executeQuery(sql);
      ResultSetMetaData metaData = query.getMetaData();

      {
        String tt = "";
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          tt += formatHeader(LEN, metaData.getColumnName(i));
        }
        logResult(tt);
      }

      int count = 0;
      while (query.next()) {
        count++;
        String tt = "";
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          tt += formatValue(LEN, query.getString(i));
        }
        logResult(tt);
      }
      logStatus(count + " results.");
    } else {
      Statement statement = c.createStatement();
      statement.execute(sql);
      logStatus("update count = " + statement.getUpdateCount());
    }
    c.close();
  }

  private void executeUpdate(Connection c, String sql) {
    logQuery(sql);
    try {
      Statement stmt = c.createStatement();
      stmt.executeUpdate(sql);
      logStatus(stmt.getUpdateCount() + " rows updated.");
    } catch (Exception e) {
      logError(e);
    }
  }

  private String formatHeader(int len, String value) {
    value = sizeValue(len, value);
    return "<span class='header value'>" + value + "</span>";
  }

  private String formatValue(int len, String value) {
    value = sizeValue(len, value);
    return "<span class='value'>" + value + "</span>";
  }

  private void log(String className, String message) {
    t += "<div class='" + className + "'>" + message + "</div>";
  }

  private void logError(Exception e) {
    logError(e.getMessage());
  }

  private void logError(String message) {
    log("error", message);
  }

  private void logQuery(String message) {
    log("query", message);
  }

  private void logResult(String message) {
    log("results", message);
  }

  private void logStatus(String message) {
    log("status", message);
  }

  private String sizeValue(int len, String value) {
    if (value == null) {
      value = "null";
    }
    if (value.length() > len) {
      value = value.substring(0, len - 1) + "É";
    } else {
      value += "                                                    ";
      value = value.substring(0, len);
    }
    return value;
  }
}
