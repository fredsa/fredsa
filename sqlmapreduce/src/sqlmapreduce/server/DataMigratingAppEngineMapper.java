package sqlmapreduce.server;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.tools.mapreduce.AppEngineMapper;

import org.apache.hadoop.io.NullWritable;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataMigratingAppEngineMapper extends
    AppEngineMapper<Key, Entity, NullWritable, NullWritable> {

  private static final Logger log = Logger.getLogger(DataMigratingAppEngineMapper.class.getName());

  private Connection connection;

  @Override
  public void cleanup(Context context) {
    log.warning("Doing per-worker cleanup");
    Sql.closeConnection(connection);
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
      if (paramList.length() == 0) {
        paramList += ", ";
        valueList += ", ";
      }
      paramList += "'" + name + "'";;
      valueList += "'" + value + "'";
    }

    String sql = "INSERT INTO foo (" + paramList + ") VALUES (" + valueList + ")";
    try {
      connection.createStatement().executeUpdate(sql);
    } catch (SQLException e) {
      log.log(Level.WARNING, "failed to insert values " + valueList, e);
      context.getCounter("foo", e.getClass().getCanonicalName()).increment(1);
    }

    context.getCounter("foo", "success").increment(1);
  }

  @Override
  public void setup(Context context) {
    log.warning("Doing per-worker setup");
    connection = Sql.getConnection();
  }

  @Override
  public void taskCleanup(Context context) {
    log.warning("Doing per-task cleanup");
  }

  @Override
  public void taskSetup(Context context) {
    log.warning("Doing per-task setup");
  }

}
