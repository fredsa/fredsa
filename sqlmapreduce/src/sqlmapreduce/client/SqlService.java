package sqlmapreduce.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("sql")
public interface SqlService extends RemoteService {
  String greetServer(String name);

  void initDatabase();
}
