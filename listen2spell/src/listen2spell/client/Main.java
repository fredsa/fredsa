package listen2spell.client;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;

import listen2spell.client.ui.GameWidget;

public class Main implements EntryPoint {
  private final WordServiceAsync wordService = GWT.create(WordService.class);

  public void onModuleLoad() {
    Log.setUncaughtExceptionHandler();
    RootLayoutPanel.get().add(new GameWidget(wordService));
  }
}
