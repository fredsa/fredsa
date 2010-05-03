package com.google.tq;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class WorkerServlet extends HttpServlet {
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    boolean requeue = false;
    resp.setContentType("text/plain");

    String limit = req.getParameter("limit");
    if (limit == null) {
      limit = "1";
    }

    String kind = req.getParameter("kind");

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Entity victim = new Entity("Victim");
    datastore.put(victim);

    Entity dragon = new Entity("Dragon");
    datastore.put(dragon);

    Entity slayer = new Entity("Slayer");
    datastore.put(slayer);

    Query query = new Query(kind).setKeysOnly();
    for (Entity entity : datastore.prepare(query).asIterable(
        FetchOptions.Builder.withLimit(Integer.parseInt(limit)))) {
      datastore.delete(entity.getKey());
      resp.getWriter().println(kind + " " + entity.getKey() + " deleted.");
      requeue = true;
    }

    if (requeue) {
      Queue queue = QueueFactory.getDefaultQueue();
      queue.add(url("/worker").param("limit", limit));
      if (kind != null) {
        queue.add(param("kind", kind));
      }
      resp.getWriter().println("Task added.");
    }

  }
}
