package com.google.tq;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.param;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class WorkerServlet extends HttpServlet {
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");
    String key = req.getParameter("key");
    key = Integer.toString(Integer.parseInt(key) + 1);
    Queue queue = QueueFactory.getDefaultQueue();
    queue.add(param("key", key));
    resp.getWriter().println("Task added.");


    // Get a handle on the datastore itself
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


    for (int i = 0; i < 20; i++) {
      Entity person = new Entity("Person");
      datastore.put(person);
    }

  }
}
