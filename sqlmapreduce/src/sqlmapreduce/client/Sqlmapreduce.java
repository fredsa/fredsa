package sqlmapreduce.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootPanel;

public class Sqlmapreduce implements EntryPoint {

  private final SqlServiceAsync service = GWT.create(SqlService.class);

  public void onModuleLoad() {
    RootPanel.get().add(new MainPage(service));
  }
}
