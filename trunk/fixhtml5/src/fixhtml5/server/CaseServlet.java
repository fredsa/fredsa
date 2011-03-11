package fixhtml5.server;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fixhtml5.client.CaseService;
import fixhtml5.shared.Case;
import fixhtml5.shared.NotLoggedInException;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class CaseServlet extends RemoteServiceServlet implements CaseService {

  private static final String URI_DONE = "/done";
  private Case c;

  @Override
  public Case getCase(String key) throws NotLoggedInException {
    checkLoggedIn();
    c = new Case();
    c.setKey(key);
    c.setName("Simple case");
    c.setHtml("<audio controls id=\"laser\" src=\"36846__EcoDTR__LaserRocket.mp3\"></audio><br/>"
        + "<button onclick=\"play('laser')\">play</button>"
        + "<div>Do not use the media controls. Instead, click on the 'play' button. Click several times, while the sound is playing."
        + "Note that the sound doesn't always play from the beginning. Sometimes it plays from somewhere in the middle.</div>");
    c.setScript("function play(id) {"
        //
        + "var e = document.getElementById(id);"
        + "try {e.pause();} catch(e) {alert(\"pause: \" + e);}"
        + "try {e.currentTime = 0;} catch(e) {alert(\"currentTime: \" + e);}"
        + "try {e.play();} catch(e) {alert(\"play: \" + e);}"
        //
        + "}");
    return c;
  }

  @Override
  public void updateCase(Case c) throws NotLoggedInException {
    checkLoggedIn();
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    ds.put(adapt(c));
  }

  private void checkLoggedIn() throws NotLoggedInException {
    UserService us = UserServiceFactory.getUserService();
    if (!us.isUserLoggedIn()) {
      throw new NotLoggedInException(us.createLoginURL(URI_DONE));
    }
  }

  private Entity adapt(Case c) {
    Entity e = new Entity(c.getKey());
    e.setUnindexedProperty("name", c.getName());
    return null;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,
      IOException {
    resp.setContentType("text/html");
    resp.getWriter().println(
        "<a href='javascript:window.close()'>close me</a><script>window.close();</script>"
            + req.getPathInfo());
    if (URI_DONE.equals(req.getPathInfo())) {
    }
  }

}
