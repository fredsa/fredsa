package fixhtml5.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import fixhtml5.shared.Case;

@RemoteServiceRelativePath("case")
public interface CaseService extends RemoteService {
  Case getCase(int number);
}
