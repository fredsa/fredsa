package com.google.tq;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.labs.taskqueue.TaskHandle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class QueryServlet extends HttpServlet {
  private static Logger LOGGER = Logger.getLogger(QueryServlet.class.getName());

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    LOGGER.info(getClass().getName());

    resp.setContentType("text/plain");

    if (!ControlBox.isRunning()) {
      return;
    }

    for (Enumeration<String> e = req.getHeaderNames(); e.hasMoreElements();) {
      String o = e.nextElement();
      LOGGER.fine(" " + o);
    }

    String limit = req.getParameter("limit");
    if (limit == null) {
      limit = "1";
    }

    String last = req.getParameter("last");

    String kind = req.getParameter("kind");

    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    Entity victim = new Entity("Victim");
    ds.put(victim);

    Entity dragon = new Entity("Dragon");
    ds.put(dragon);

    Entity slayer = new Entity("Slayer");
    ds.put(slayer);

    Query query = new Query(kind).setKeysOnly();
    if (last != null) {
      query.addFilter("__key__", FilterOperator.GREATER_THAN, KeyFactory.stringToKey(last));
    }
    FetchOptions options = FetchOptions.Builder.withLimit(Integer.parseInt(limit));

    List<Key> keys = new ArrayList<Key>();
    for (Entity entity : ds.prepare(query).asIterable(options)) {
      keys.add(entity.getKey());
      LOGGER.info(kind + " " + entity.getKey() + " added to list of keys to delete.");
    }

    if (keys.size() > 0) {
      TaskHandle handle1 = TaskUtil.enqueueQueryItem(kind, limit, keys.get(keys.size() - 1));
      LOGGER.info("Task added: " + handle1.getName());

      TaskHandle handle2 = TaskUtil.enqueueDeleteItem(kind, keys);
      LOGGER.info("Task added: " + handle2.getName());
    }

  }
}
