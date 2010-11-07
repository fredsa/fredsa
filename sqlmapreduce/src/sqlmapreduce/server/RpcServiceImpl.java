package sqlmapreduce.server;

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

  private static final String SELECT_STAR_FROM = "select * from ";

  private String t;

  public String executeDatastoreQuery(String sql) {
    t = "";
    String[] queries = sql.split(";");
    for (String query : queries) {
      query = query.trim();
      try {
        doDatastoreQuery(query);
      } catch (SQLException e) {
        t += "<div class='error'>" + e.getMessage() + "</div>";
      }
    }
    return t;
  }

  public String executeRelationalQuery(String sql) {
    t = "";
    String[] queries = sql.split(";");
    for (String query : queries) {
      query = query.trim();
      try {
        doRelationalQuery(query);
      } catch (SQLException e) {
        t += "<div class='error'>" + e.getMessage() + "</div>";
      }
    }
    return t;
  }

  public String initDatastore() {
    t = "";

    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    String[] FIRST_NAMES = {"Ford", "Arthur", "Zaphod", "Tricia"};
    String[] LAST_NAMES = {"Prefect", "Dent", "Beeblebrox", "McMillan"};

    for (String first : FIRST_NAMES) {
      for (String last : LAST_NAMES) {
        String kind = Constants.KIND;
        Entity entity = new Entity(kind);
        entity.setProperty("first_name", first);
        entity.setProperty("last_name", last);
        t += "<div class='results'>Kind=" + kind + "(first_name=" + first + ", last_name=" + last
            + ")</div>";
        ds.put(entity);
      }
    }
    return t;
  }

  public String initRelational() {
    t = "";
    Connection c = Constants.getConnection();

    executeUpdate(c, "drop table contact;");
    executeUpdate(c, "drop table emloyee;");
    executeUpdate(c, "create table employee ( id int, name varchar(200) )");
    executeUpdate(c, "insert into employee values(1, 'Ford Prefect')");
    executeUpdate(c, "insert into employee values(42, 'Fred Sauer')");

    return t;
  }

  private void doDatastoreQuery(String sql) throws SQLException {
    t += "<div class='query'>" + sql + "</div>";
    sql = sql.replaceAll("\\s+", " ").trim().toLowerCase();
    if (!sql.startsWith(SELECT_STAR_FROM)) {
      t += "<div class='error'>Unrecognized GQL query</div>";
      return;
    }
    String table = sql.substring(SELECT_STAR_FROM.length());
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery prepared = ds.prepare(new Query(table));
    List<Entity> results = prepared.asList(Builder.withDefaults());
    for (Entity entity : results) {
      Map<String, Object> props = entity.getProperties();
      String propList = "";
      for (Entry<String, Object> entry : props.entrySet()) {
        String name = entry.getKey();
        String value = "" + entry.getValue();
        if (propList.length() > 0) {
          propList += "\t";
        }
        propList += name + ": " + value;
      }
      logResults(propList);
    }
    t += "<div class='status'>" + results.size() + " results.</div>";
  }

  private void logResults(String propList) {
    t += "<div class='results'>" + propList + "</div>";
  }

  private void doRelationalQuery(String sql) throws SQLException {
    Connection c = Constants.getConnection();

    t += "<div class='query'>" + sql + "</div>";
    if (sql.trim().toLowerCase().startsWith("select")) {
      ResultSet query = c.createStatement().executeQuery(sql);
      ResultSetMetaData metaData = query.getMetaData();
      int count =0;
      while (query.next()) {
        count++;
        t += "<div class='results'>";
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          if (i > 1) {
            t += "\t";
          }
          t += query.getString(i);
        }
        t += "</div>";
      }
      t += "<div class='status'>" + count + " results.</div>";
    } else {
      Statement statement = c.createStatement();
      statement.execute(sql);
      t += "<div class='status'>update count = " + statement.getUpdateCount() + "</div>";
    }
    c.close();
  }

  private void executeUpdate(Connection c, String sql) {
    t = "<div class='query'>" + sql + "</div>";
    try {
      Statement stmt = c.createStatement();
      stmt.executeUpdate(sql);
      t += "<div class='status'>" + stmt.getUpdateCount() + " rows updated.</div>";
    } catch (Exception e) {
      t += "<div class='error'>" + e.getMessage() + "</div>";
    }
  }
}
