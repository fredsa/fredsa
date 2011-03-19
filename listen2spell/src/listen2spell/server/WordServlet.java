package listen2spell.server;

import com.allen_sauer.gwt.log.client.Log;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.repackaged.com.google.common.util.Base64;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import java.net.URL;
import java.util.HashMap;

import listen2spell.client.WordService;
import listen2spell.shared.Word;

@SuppressWarnings("serial")
public class WordServlet extends RemoteServiceServlet implements WordService {

  private static final String KIND = "Word";
  private Word nothing = new Word("", "data:");
  private HashMap<String, String> wordMap;

  public WordServlet() {
    wordMap = new HashMap<String, String>();
    wordMap.put("a", "eh"); // say 'eh', not 'uh'
  }

  public Word[] getWords(String word) throws IllegalArgumentException {
    if (word == null || word.trim().length() == 0) {
      return new Word[] {nothing};
    }
    word = map(word.toLowerCase());
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    URLFetchService fs = URLFetchServiceFactory.getURLFetchService();
    MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
    try {
      Entity entity;
      String dataUrl;
      Word w;
      Log.debug("memcache get()...");
      w = (Word) ms.get(word);
      if (w == null) {
        try {
          Log.debug("datastore get()...");
          entity = ds.get(KeyFactory.createKey(KIND, word));
          dataUrl = ((Text) entity.getProperty("dataUrl")).getValue();
        } catch (EntityNotFoundException e) {
          URL url = new URL("http://translate.google.com/translate_tts?ie=UTF-8&tl=en&prev=input&q="
              + word);
          Log.debug("url fetch(" + url + ")...");
          HTTPResponse resp = fs.fetch(url);
          byte[] content = resp.getContent();
          dataUrl = "data:audio/mpeg;base64," + Base64.encode(content);

          entity = new Entity(KIND, word);
          entity.setProperty("word", word);
          entity.setUnindexedProperty("dataUrl", new Text(dataUrl));
          Log.debug("datastore put()...");
          ds.put(entity);
        }
        w = new Word(word, dataUrl);
        ms.put(word, w);
      }

      return new Word[] {w};
    } catch (Exception e) {
      Log.error("oops", e);
      throw new RuntimeException(e.toString());
    }
  }

  private String map(String word) {
    String mappedWord = wordMap.get(word);
    return mappedWord != null ? mappedWord : word;
  }
}
