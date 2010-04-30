package com.google.tq;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class WorkerServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");
    String key = req.getParameter("key");
    key = Integer.toString(Integer.parseInt(key) + 1);
    Queue queue = QueueFactory.getDefaultQueue();
    queue.add(param("key", key));
    resp.getWriter().println("Task added.");
  }
}
