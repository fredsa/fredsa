package sqlmapreduce.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>SqlService</code>.
 */
public interface SqlServiceAsync {
  void greetServer(String input, AsyncCallback<String> callback);
}
