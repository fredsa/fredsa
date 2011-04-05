package filemanager.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class FilemanagerEntryPoint implements EntryPoint {
  @Override
  public void onModuleLoad() {
    RootLayoutPanel.get().add(new UploadFormWidget());
  }
}
