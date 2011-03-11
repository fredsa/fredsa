package fixhtml5.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fixhtml5.client.CaseService;
import fixhtml5.shared.Case;

@SuppressWarnings("serial")
public class CaseServlet extends RemoteServiceServlet implements CaseService {

  @Override
  public Case getCase(int number) throws IllegalArgumentException {
    return new Case();
  }

}
