// Copyright 2010 Google Inc. All Rights Reserved.

package guestbook;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GuestbookServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    Entity greeting = new Entity("Greeting");
    greeting.setProperty("author", "Ford");
    ds.put(greeting);
    resp.setContentType("text/plain");
    resp.getWriter().println("Ford signed guestbook.");
  }
}
