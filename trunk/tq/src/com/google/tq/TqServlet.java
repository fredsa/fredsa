package com.google.tq;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class TqServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    resp.setContentType("text/plain");
    Queue queue = QueueFactory.getDefaultQueue();
    queue.add(url("/worker"));
    resp.getWriter().println("Task added.");
  }
}
