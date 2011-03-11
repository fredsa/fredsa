package fixhtml5.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fixhtml5.client.CaseService;
import fixhtml5.shared.Case;

@SuppressWarnings("serial")
public class CaseServlet extends RemoteServiceServlet implements CaseService {

  private Case c;

  @Override
  public Case getCase(int number) {
    c = new Case();
    c.setName("Simple case");
    c.setHtml("this is <b> html </b>!");
    c.setScript("alert(42)");
    return c;
  }

}
