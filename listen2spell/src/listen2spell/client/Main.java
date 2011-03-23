package listen2spell.client;

import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class Main implements EntryPoint {
  private Speaker speaker;
  private final WordServiceAsync wordService = GWT.create(WordService.class);

  public void onModuleLoad() {
    Log.setUncaughtExceptionHandler();
    RootPanel.get().add(Log.getLogger(DivLogger.class).getWidget());
    wordService.getWordList(new AsyncCallback<String[]>() {
      public void onFailure(Throwable e) {
        Log.error("failed to get words from server", e);
      }

      public void onSuccess(String[] words) {
        show(words[0]);
      }
    });
    speaker = new Speaker();
    speaker.setService(wordService);
  }

  protected void show(String word) {
    RootPanel.get().setStylePrimaryName("answer");
    Challenge challenge = new Challenge(word);
    challenge.setSpeaker(speaker);
    RootPanel.get().add(challenge);
    speaker.speak(word);
  }
}
