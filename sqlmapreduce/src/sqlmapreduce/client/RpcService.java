package sqlmapreduce.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc")
public interface RpcService extends RemoteService {
  String executeDatastoreQuery(String sql);

  String executeRelationalQuery(String sql);

  String initDatastore();

  String initRelational();
}
