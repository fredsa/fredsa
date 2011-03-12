package fixhtml5.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fixhtml5.shared.Case;

public interface CaseServiceAsync {
  void updateCase(Case c, AsyncCallback<Void> callback);

  void getCase(String key, AsyncCallback<Case> callback);

  void getPrettyUserAgent(String userAgentString, AsyncCallback<String> callback);
}
