package listen2spell.client;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.voices.client.Html5Sound;
import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

import listen2spell.shared.Word;

public class Main implements EntryPoint {
  private final WordServiceAsync worService = GWT.create(WordService.class);

  public void onModuleLoad() {
    worService.getWords(null, new AsyncCallback<Word[]>() {
      public void onFailure(Throwable e) {
        Log.error("failed to get words from server", e);
      }

      public void onSuccess(Word[] result) {
        show(result[0]);
      }
    });
  }

  @SuppressWarnings("deprecation")
  protected void show(Word w) {
    String word = w.getWord();
    SoundController sc = new SoundController();
    sc.setPreferredSoundType(Html5Sound.class);
    String url = w.getUrl();
    HTML html = new HTML("<h1>" + word + "</h1>");
    html.setStylePrimaryName("answer");
    RootPanel.get().add(html);
    Sound sound = sc.createSound("audio/mpeg", url);
    sound.play();
  }
}
