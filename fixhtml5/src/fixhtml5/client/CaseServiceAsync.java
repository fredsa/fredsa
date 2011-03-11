package fixhtml5.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fixhtml5.shared.Case;

public interface CaseServiceAsync {
  void getCase(int number, AsyncCallback<Case> callback);
}
