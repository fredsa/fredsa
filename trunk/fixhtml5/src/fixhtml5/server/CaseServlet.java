package fixhtml5.server;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fixhtml5.client.CaseService;
import fixhtml5.shared.Case;
import fixhtml5.shared.NotLoggedInException;

import java.util.logging.Logger;

@SuppressWarnings("serial")
public class CaseServlet extends RemoteServiceServlet implements CaseService {

  private static final Logger logger = Logger.getLogger(CaseServlet.class.getName());

  @Override
  public Case getCase(String keyName) throws NotLoggedInException {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    try {
      Key key = KeyFactory.createKey(Case.class.getSimpleName(), keyName);
      Entity entity = ds.get(key);
      Case c = adapt(entity);
      return c;
    } catch (EntityNotFoundException e) {
      Case c = new Case();
      c.setKey(keyName);
      c.setName("Simple case");
      c.setHtml("<audio controls id=\"laser\" src=\"36846__EcoDTR__LaserRocket.mp3\"></audio><br/>"
          + "<save onclick=\"play('laser')\">play</save>"
          + "<div>Do not use the media controls. Instead, click on the 'play' save. Click several times, while the sound is playing."
          + "Note that the sound doesn't always play from the beginning. Sometimes it plays from somewhere in the middle.</div>");
      c.setScript("function play(id) {"
          //
          + "var e = document.getElementById(id);"
          + "try {e.pause();} catch(e) {alert(\"pause: \" + e);}"
          + "try {e.currentTime = 0;} catch(e) {alert(\"currentTime: \" + e);}"
          + "try {e.play();} catch(e) {alert(\"play: \" + e);}"
          //
          + "}");
      updateCase(c);
      throw new RuntimeException(e);
      //      return c;
    }
  }

  private Case adapt(Entity entity) {
    Case c = new Case();
    c.setKey(entity.getKey().getName());
    c.setName((String) entity.getProperty("name"));
    c.setHtml((String) entity.getProperty("html"));
    c.setScript((String) entity.getProperty("script"));
    return c;
  }

  @Override
  public void updateCase(Case c) throws NotLoggedInException {
    checkLoggedIn();
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    logger.info("udpateCase(" + c + ")");
    ds.put(adapt(c));
  }

  private void checkLoggedIn() throws NotLoggedInException {
    UserService us = UserServiceFactory.getUserService();
    if (!us.isUserLoggedIn()) {
      throw new NotLoggedInException();
    }
  }

  private Entity adapt(Case c) {
    Entity entity = new Entity(c.getClass().getSimpleName(), c.getKey());
    entity.setUnindexedProperty("name", c.getName());
    entity.setUnindexedProperty("script", c.getScript());
    entity.setUnindexedProperty("html", c.getHtml());
    return entity;
  }

}
