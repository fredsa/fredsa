package sqlmapreduce.server;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import sqlmapreduce.client.RpcService;
import sqlmapreduce.shared.Constants;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings("serial")
public class RpcServiceImpl extends RemoteServiceServlet implements RpcService {

  private static final String SELECT_STAR_FROM = "select * from ";

  public String executeDatastoreQuery(String sql) {
    String t = "";
    String[] queries = sql.split(";");
    for (String query : queries) {
      query = query.trim();
      try {
        t += doDatastoreQuery(query);
      } catch (SQLException e) {
        t += "<div class='error'>" + e.getMessage() + "</div>";
      }
    }
    return t;
  }

  public String executeRelationalQuery(String sql) {
    String t = "";
    String[] queries = sql.split(";");
    for (String query : queries) {
      query = query.trim();
      try {
        t += doRelationalQuery(query);
      } catch (SQLException e) {
        t += "<div class='error'>" + e.getMessage() + "</div>";
      }
    }
    return t;
  }

  public String initDatastore() {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    String[] FIRST_NAMES = {"Ford", "Arthur", "Zaphod", "Tricia"};
    String[] LAST_NAMES = {"Prefect", "Dent", "Beeblebrox", "McMillan"};

    String t = "";
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
    String t = "";
    Connection c = Constants.getConnection();

    t += executeUpdate(c, "drop table contact;");
    t += executeUpdate(c, "drop table emloyee;");
    t += executeUpdate(c, "create table employee ( id int, name varchar(200) )");
    t += executeUpdate(c, "insert into employee values(1, 'Ford Prefect')");
    t += executeUpdate(c, "insert into employee values(42, 'Fred Sauer')");

    return t;
  }

  private String doDatastoreQuery(String sql) throws SQLException {
    String t = "";
    t += "<div class='query'>" + sql + "</div>";
    sql = sql.replaceAll("\\s+", " ").trim().toLowerCase();
    if (!sql.startsWith(SELECT_STAR_FROM)) {
      t += "<div class='error'>Unrecognized GQL query</div>";
      return t;
    }
    String table = sql.substring(SELECT_STAR_FROM.length());
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery prepared = ds.prepare(new Query(table));
    for (Iterator<Entity> iterator = prepared.asIterator(); iterator.hasNext();) {
      Entity entity = iterator.next();
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
      t += "<div class='results'>" + propList + "</div>";
    }
    return t;
  }

  private String doRelationalQuery(String sql) throws SQLException {
    String t = "";
    Connection c = Constants.getConnection();

    t += "<div class='query'>" + sql + "</div>";
    if (sql.trim().toLowerCase().startsWith("select")) {
      ResultSet query = c.createStatement().executeQuery(sql);
      ResultSetMetaData metaData = query.getMetaData();
      while (query.next()) {
        t += "<div class='results'>";
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          if (i > 1) {
            t += "\t";
          }
          t += query.getString(i);
        }
        t += "</div>";
      }
    } else {
      Statement statement = c.createStatement();
      statement.execute(sql);
      t += "<div class='status'>update count = " + statement.getUpdateCount() + "</div>";
    }
    c.close();
    return t;
  }

  private String executeUpdate(Connection c, String sql) {
    String t = "<div class='query'>" + sql + "</div>";
    try {
      Statement stmt = c.createStatement();
      stmt.executeUpdate(sql);
      t += "<div class='status'>" + stmt.getUpdateCount() + " rows updated.</div>";
    } catch (Exception e) {
      t += "<div class='error'>" + e.getMessage() + "</div>";
    }
    return t;
  }
}
