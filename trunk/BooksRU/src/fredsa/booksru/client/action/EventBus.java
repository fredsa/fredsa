package fredsa.booksru.client.action;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class EventBus {

  private HashMap<Class<?>, ArrayList<AsyncCallback<?>>> map;

  public <T extends Response> void addHandler(Class<T> type, AsyncCallback<T> callback) {
    ArrayList<AsyncCallback<?>> list = map.get(type);
    if (list == null) {
      list = new ArrayList<AsyncCallback<?>>();
    }
    list.add(callback);
    map.put(type, list);
  }

  public <T extends Response> void fire(T response) {
    ArrayList<AsyncCallback<?>> list = map.get(response.getClass());
    if (list != null) {
      for (AsyncCallback<?> callback : list) {
        AsyncCallback<T> cb = (AsyncCallback<T>) callback;
        cb.onSuccess(response);
      }
    }
  }

}
