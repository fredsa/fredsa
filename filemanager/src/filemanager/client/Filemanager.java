package filemanager.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class Filemanager implements EntryPoint {
  @Override
  public void onModuleLoad() {
    RootPanel.get().add(new Label("Hi"));
  }
}
