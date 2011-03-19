package listen2spell.client;

import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

import listen2spell.shared.Word;

public class Main implements EntryPoint {
  private Speaker speaker;
  private final WordServiceAsync wordService = GWT.create(WordService.class);

  public void onModuleLoad() {
    Log.setUncaughtExceptionHandler();
    wordService.getWords("beautiful", new AsyncCallback<Word[]>() {
      public void onFailure(Throwable e) {
        Log.error("failed to get words from server", e);
      }

      public void onSuccess(Word[] result) {
        show(result[0]);
      }
    });
    speaker = new Speaker();
    speaker.setService(wordService);
  }

  @SuppressWarnings("deprecation")
  protected void show(Word w) {
    String word = w.getWord();
    String url = w.getUrl();
    HTML html = new HTML("<h1>" + word + "</h1>");
    RootPanel.get().setStylePrimaryName("answer");
    Challenge challenge = new Challenge(word);
    challenge.setSpeaker(speaker);
    RootPanel.get().add(challenge);
    speaker.speak("beautiful");
  }
}
