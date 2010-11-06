package sqlmapreduce.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RpcServiceAsync {
  void executeDatastoreQuery(String sql, AsyncCallback<String> callback);

  void executeRelationalQuery(String sql, AsyncCallback<String> callback);

  void initDatastore(AsyncCallback<String> callback);

  void initRelational(AsyncCallback<String> callback);
}
