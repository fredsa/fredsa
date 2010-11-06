package sqlmapreduce.server;

import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.FetchOptions.Builder;
import com.google.appengine.tools.mapreduce.AppEngineMapper;
import com.google.appengine.tools.mapreduce.DatastoreInputFormat;

import org.apache.hadoop.io.NullWritable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataMigratingMapper extends AppEngineMapper<Key, Entity, NullWritable, NullWritable> {

  private static final Logger log = Logger.getLogger(DataMigratingMapper.class.getName());
  private Connection connection;

  private String kind;

  @Override
  public void cleanup(Context context) {
    log.warning("Doing per-worker cleanup");
  }

  @Override
  public void map(Key key, Entity entity, Context context) {
    log.warning("Mapping key: " + key);
    Map<String, Object> props = entity.getProperties();
    String paramList = "";
    String valueList = "";
    for (Entry<String, Object> entry : props.entrySet()) {
      String name = entry.getKey();
      String value = "" + entry.getValue();
      if (paramList.length() > 0) {
        paramList += ", ";
        valueList += ", ";
      }
      paramList += "\"" + name + "\"";
      valueList += "'" + value + "'";
    }

    String sql = "INSERT INTO " + kind + " (" + paramList + ") VALUES (" + valueList + ")";
    log.info(sql);
    try {
      connection.createStatement().executeUpdate(sql);
    } catch (SQLException e) {
      log.log(Level.WARNING, "failed to insert values " + valueList + " due to " + e.getMessage());
      context.getCounter("foo", e.getClass().getCanonicalName()).increment(1);
    }

    context.getCounter("foo", "success").increment(1);
  }

  @Override
  public void setup(Context context) {
    log.warning("Doing per-worker setup");
    String kind = context.getConfiguration().get(DatastoreInputFormat.ENTITY_KIND_KEY);
    Entity entity = getFirstEntity(kind);

    Map<String, Object> props = entity.getProperties();
    String columnList = "";
    for (Entry<String, Object> entry : props.entrySet()) {
      String name = entry.getKey();
      Object value = entry.getValue();
      if (columnList.length() > 0) {
        columnList += ", ";
      }
      columnList += "\"" + name + "\" VARCHAR(2000)";
    }

    Connection c = Sql.getConnection();
    String sql = "CREATE TABLE " + kind + "(" + columnList + ")";
    try {
      c.createStatement().execute(sql);
    } catch (SQLException e) {
      log.log(Level.WARNING, "Failed to create table due to " + e.getMessage());
      //      throw new RuntimeException(e);
    } finally {
      try {
        c.close();
      } catch (SQLException ignore) {
      }
    }
  }

  @Override
  public void taskCleanup(Context context) {
    log.warning("Doing per-task cleanup");
    Sql.closeConnection(connection);
  }

  @Override
  public void taskSetup(Context context) {
    log.warning("Doing per-task setup");
    kind = context.getConfiguration().get(DatastoreInputFormat.ENTITY_KIND_KEY);
    connection = Sql.getConnection();
  }

  private Entity getFirstEntity(String kind) {
    Query query = new Query(kind);
    List<Entity> results = DatastoreServiceFactory.getDatastoreService().prepare(query).asList(Builder.withLimit(1));
    return results.get(0);
  }

}
