package com.google.cache;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class CacheServlet extends HttpServlet {
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
    int secs = ageText == null ? 10 : Integer.parseInt(ageText);
    resp.setHeader("Cache-Control", "public, s-maxage=" + secs);
    resp.setContentType("text/plain");
    resp.getWriter().println("age=" + secs);
    for (Enumeration<String> e = req.getHeaderNames(); e.hasMoreElements();) {
      String key = e.nextElement();
      resp.getWriter().println(key+": ");
      for (Enumeration<String> e2 = req.getHeaders(key); e2.hasMoreElements();) {
        String value =e2.nextElement();
        resp.getWriter().println(" - " + value);
      }
    }
    //    resp.getWriter().println(userService.isUserAdmin() ? "admin" : "user");
  }
}
