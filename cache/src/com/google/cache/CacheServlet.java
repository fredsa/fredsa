package com.google.cache;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class CacheServlet extends HttpServlet {
  private DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.FULL,
      SimpleDateFormat.FULL);

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    //    UserService userService = UserServiceFactory.getUserService();
    //    if (!userService.isUserLoggedIn()) {
    //      resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
    //      return;
    //    }

    String sleepText = req.getParameter("sleep");
    if (sleepText != null) {
      try {
        Thread.sleep(Integer.parseInt(sleepText));
      } catch (InterruptedException ignore) {
      }
    }

    String ageText = req.getParameter("age");
    int age = Integer.parseInt(ageText);

    //  Date: <Date>
    //  Expires: <Date + age>
    //  Cache-control: public, max-age=<age>

    Date now = new Date();
    resp.setDateHeader("Date", now.getTime());
    resp.setDateHeader("Expires", now.getTime() + 1000L * age);
    resp.setHeader("Cache-Control", "public, max-age=" + age);

    resp.setContentType("text/plain");
    resp.getWriter().println(df.format(now) + " " + System.nanoTime());

    String reqText = req.getParameter("req");
    if (reqText != null) {
      for (Enumeration<String> e = req.getHeaderNames(); e.hasMoreElements();) {
        String key = e.nextElement();
        resp.getWriter().println(key + ": ");
        for (Enumeration<String> e2 = req.getHeaders(key); e2.hasMoreElements();) {
          String value = e2.nextElement();
          resp.getWriter().println(" - " + value);
        }
      }
      //    resp.getWriter().println(userService.isUserAdmin() ? "admin" : "user");
    }
  }
}
