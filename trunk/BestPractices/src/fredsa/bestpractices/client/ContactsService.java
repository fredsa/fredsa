package fredsa.bestpractices.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("contacts")
public interface ContactsService extends RemoteService {
  <T extends Response> T execute(Action<T> action);
}
