package sqlmapreduce.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RpcServiceAsync {
  void executeQuery(String sql, AsyncCallback<String> callback);

  void initDatabase(AsyncCallback<String> callback);

  void initSql(AsyncCallback<String> callback);
}
