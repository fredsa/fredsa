package fredsa.bestpractices.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fredsa.bestpractices.client.Action;
import fredsa.bestpractices.client.ContactsService;
import fredsa.bestpractices.client.Response;
import fredsa.bestpractices.client.UpdateContactsResponse;

public class ContactsServiceImpl extends RemoteServiceServlet implements ContactsService {

  public <T extends Response> T execute(Action<T> action) {
    return (T) new UpdateContactsResponse();
  }

}
