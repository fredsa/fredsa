package com.google.tq;

import com.google.appengine.api.labs.taskqueue.TaskHandle;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class TqServlet extends HttpServlet {
  private static Logger LOGGER = Logger.getLogger(TqServlet.class.getName());

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    LOGGER.info(getClass().getName());

    resp.setContentType("text/html");
    resp.getWriter().println("<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'>");
    resp.getWriter().println("<html>");
    resp.getWriter().println("<head>");
    resp.getWriter().println("<meta http-equiv='content-type' content='text/html; charset=UTF-8'>");
    resp.getWriter().println("<title>TQ Test App</title>");
    resp.getWriter().println("</head>");
    resp.getWriter().println("<body>");
    resp.getWriter().println("<h1>TQ Test App</h1>");

    resp.getWriter().println("<br />");
    resp.getWriter().println("<a href='/tq'>HOME</a>");
    resp.getWriter().println("<br />");
    resp.getWriter().println("<a href='/tq/stop'>Ask all tasks to stop</a>");
    resp.getWriter().println("<br />");
    resp.getWriter().println("<a href='/tq/start?kind=Dragon&limit=5'>Start Dragon a task</a>");
    resp.getWriter().println("<br />");

    resp.getWriter().println("<hr />");
    resp.getWriter().println("<br />");

    String pathInfo = req.getPathInfo();
    if (pathInfo != null) {
      if (pathInfo.equals("/start")) {
        ControlBox.start();
        TaskHandle handle =
            TaskUtil.enqueueQueryItem(req.getParameter("kind"), req.getParameter("limit"), null);
        resp.getWriter().println("Running flag set and task added: " + handle.getName());
      }
      if (pathInfo.equals("/stop")) {
        ControlBox.stop();
        resp.getWriter().println("Running flag cleared");
      }
    }

    resp.getWriter().println("</body>");
    resp.getWriter().println("</html>");
  }
}
