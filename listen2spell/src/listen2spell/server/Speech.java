package listen2spell.server;

import com.allen_sauer.gwt.log.client.Log;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.repackaged.com.google.common.util.Base64;

import java.net.URL;
import java.util.HashMap;

import listen2spell.shared.Spoken;

public class Speech {
  private static final String KIND = "Spoken";

  private static HashMap<String, String> wordMap;

  static {
    wordMap = new HashMap<String, String>();
    wordMap.put("a", "eh"); // say 'eh', not 'uh'
  }

  static Spoken getWord(String word) {
    if (word == null || word.trim().length() == 0) {
      return Spoken.EMPTY;
    }
    word = map(word.toLowerCase());
    DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
    URLFetchService fs = URLFetchServiceFactory.getURLFetchService();
    MemcacheService ms = MemcacheServiceFactory.getMemcacheService();
    try {
      Entity entity;
      byte[] data;
      String url;
      Spoken spoken;
      Log.debug("memcache get()...");
      spoken = (Spoken) ms.get(word);
      if (spoken == null) {
        try {
          Log.debug("datastore get()...");
          entity = ds.get(KeyFactory.createKey(KIND, word));
          data = ((Blob) entity.getProperty("data")).getBytes();
        } catch (EntityNotFoundException e) {
          URL u = new URL("http://translate.google.com/translate_tts?ie=UTF-8&tl=en&prev=input&q="
              + word);
          Log.debug("url fetch(" + u + ")...");
          HTTPResponse resp = fs.fetch(u);
          data = resp.getContent();

          entity = new Entity(KIND, word);
          entity.setProperty("word", word);
          entity.setUnindexedProperty("data", new Blob(data));
          Log.debug("datastore put()...");
          ds.put(entity);
        }
        url = "data:audio/mpeg;base64," + Base64.encode(data);
        url = "/listen2spell/speech?q=" + word;
        spoken = new Spoken(word, url, data);
      }
      return spoken;
    } catch (Exception e) {
      Log.error("oops", e);
      throw new RuntimeException(e.toString());
    }
  }

  private static String map(String word) {
    String mappedWord = wordMap.get(word);
    return mappedWord != null ? mappedWord : word;
  }
}
