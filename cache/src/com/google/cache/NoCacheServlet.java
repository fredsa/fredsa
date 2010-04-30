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
public class NoCacheServlet extends HttpServlet {
  private DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.FULL,
      SimpleDateFormat.FULL);

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    //  Date: <Date>
    //  Expires: Mon, 01 Jan 1990 00:00:00 GMT
    //  Pragma: no-cache
    //  Cache-control: no-cache, must-revalidate

    Date now = new Date();
    resp.setDateHeader("Date", now.getTime());
    resp.setDateHeader("Expires", 0);
    resp.setHeader("Pragma", "no-cache");
    resp.setHeader("Cache-Control", "no-cache, must-revalidate");

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
    }
  }
}
