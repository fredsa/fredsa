package fredsa.bestpractices.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ContactsServiceAsync {
  <T extends Response> void execute(Action<T> action, AsyncCallback<T> callback);
}