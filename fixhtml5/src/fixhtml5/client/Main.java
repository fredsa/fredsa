package fixhtml5.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Main implements EntryPoint {

  private final CaseServiceAsync caseService = GWT.create(CaseService.class);

  public void onModuleLoad() {
    RootLayoutPanel.get().add(new Label("test"));
  }
}
