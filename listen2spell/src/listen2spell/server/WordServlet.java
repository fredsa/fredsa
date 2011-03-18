package listen2spell.server;

import com.allen_sauer.gwt.log.client.Log;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.net.URL;

import listen2spell.client.WordService;
import listen2spell.shared.Word;

@SuppressWarnings("serial")
public class WordServlet extends RemoteServiceServlet implements WordService {

  private static final String KIND = "Word";

  public Word[] getWords(String input) throws IllegalArgumentException {
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    URLFetchService fs = URLFetchServiceFactory.getURLFetchService();
    try {
      String word = "squid";

      Entity entity;
      String dataUrl;
      try {
        Log.debug("get()...");
        entity = ds.get(KeyFactory.createKey(KIND, word));
        dataUrl = (String) entity.getProperty("dataUrl");
      } catch (EntityNotFoundException e) {
        URL url = new URL("http://translate.google.com/translate_tts?ie=UTF-8&tl=en&prev=input&q="
            + word);
        Log.debug("fetch(" + url + ")...");
        HTTPResponse resp = fs.fetch(url);
        byte[] content = resp.getContent();
        dataUrl = "data:audio/mpeg;base64," + Base64.encode(content);

        entity = new Entity(KIND);
        entity.setProperty("word", word);
        entity.setUnindexedProperty("dataUrl", new Text(dataUrl));
        Log.debug("put()...");
        ds.put(entity);
      }

      Word w = new Word(word, dataUrl);
      return new Word[] {w};
    } catch (Exception e) {
      Log.error("oops", e);
      throw new RuntimeException(e.toString());
    }
  }
}
