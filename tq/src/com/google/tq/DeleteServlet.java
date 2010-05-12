package com.google.tq;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DeleteServlet extends HttpServlet {
  private static Logger LOGGER = Logger.getLogger(DeleteServlet.class.getName());

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    LOGGER.info(getClass().getName());

    resp.setContentType("text/plain");

    if (!ControlBox.isRunning()) {
      return;
    }

    String[] keys = req.getParameterValues("keys");

    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();

    List<Key> keyList = new ArrayList<Key>();
    for (String key : keys) {
      Key k = KeyFactory.stringToKey(key);
      LOGGER.info("Planning to delete key " + k);
      keyList.add(k);
    }
    ds.delete(keyList);
  }
}
