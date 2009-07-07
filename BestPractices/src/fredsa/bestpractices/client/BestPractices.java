package fredsa.bestpractices.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class BestPractices implements EntryPoint {

  private final ContactsServiceAsync contactService = new CachingContactService();

  public void onModuleLoad() {
    contactService.execute(new UpdateContact(), new AsyncCallback<UpdateContactsResponse>() {

      public void onFailure(Throwable caught) {
        caught.printStackTrace();
      }

      public void onSuccess(UpdateContactsResponse result) {
        System.err.println("Client code got result " + result);
        RootPanel.get().add(new Label("result: " + result));
      }

    });
  }
}
