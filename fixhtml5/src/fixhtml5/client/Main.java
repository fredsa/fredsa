package fixhtml5.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import fixhtml5.shared.Case;

public class Main implements EntryPoint {

  private final CaseServiceAsync caseService = GWT.create(CaseService.class);

  public void onModuleLoad() {
    final Element wait = Document.get().getElementById("wait");
    wait.setInnerHTML("Fetching cases...");
    caseService.getCase(0, new AsyncCallback<Case>() {
      @Override
      public void onSuccess(Case result) {
        wait.removeFromParent();
        RootPanel.getBodyElement().appendChild(new CaseWidget(result).getElement());
      }

      @Override
      public void onFailure(Throwable caught) {
        wait.setInnerHTML("FAILED TO LOAD CASES: " + caught);
      }
    });
  }
}
