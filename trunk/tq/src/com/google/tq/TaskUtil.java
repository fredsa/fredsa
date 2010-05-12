package com.google.tq;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskHandle;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.appengine.api.labs.taskqueue.TaskOptions.Method;

import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;

@SuppressWarnings("serial")
public class TaskUtil extends HttpServlet {

  private static Logger LOGGER = Logger.getLogger(TaskUtil.class.getName());


  public static TaskHandle enqueueQueryItem(String kind, String limit, Key lastKey) {
    Queue queue = QueueFactory.getQueue("query");
    TaskOptions options = url("/query/").param("limit", limit);
    if (kind != null) {
      options.param("kind", kind);
    }
    if (lastKey != null) {
      options.param("last", KeyFactory.keyToString(lastKey));
    }
    TaskHandle handle = queue.add(options);
    LOGGER.info("Query(kind=" + kind + ", limit=" + limit + ") task added: " + handle.getName());
    return handle;
  }

  public static TaskHandle enqueueDeleteItem(String kind, Iterable<Key> keys) {
    Queue queue = QueueFactory.getQueue("delete");
    TaskOptions options = url("/delete/").method(Method.POST);
    for (Key key : keys) {
      options.param("keys", KeyFactory.keyToString(key));
    }
    TaskHandle handle = queue.add(options);
    LOGGER.info("Delete(keys=" + keys + ") task added: " + handle.getName());
    return handle;
  }
}
