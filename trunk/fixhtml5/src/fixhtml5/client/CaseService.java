package fixhtml5.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import fixhtml5.shared.Case;
import fixhtml5.shared.NotLoggedInException;

@RemoteServiceRelativePath("case")
public interface CaseService extends RemoteService {
  Case getCase(String key) throws NotLoggedInException;

  void updateCase(Case c) throws NotLoggedInException;
}
