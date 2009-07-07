package fredsa.bestpractices.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CachingContactService implements ContactsServiceAsync {

  private final ContactsServiceAsync realService = GWT.create(ContactsService.class);

  public <T extends Response> void execute(Action<T> action, final AsyncCallback<T> cb) {
    realService.execute(action, new AsyncCallback<T>() {

      public void onFailure(Throwable caught) {
        cb.onFailure(caught);
      }

      public void onSuccess(T result) {
        System.err.println("This is where we would cache " + result);
        cb.onSuccess(result);
        System.err.println("This is where we would fire the event on the bus " + result);
      }

    });
  }

}
