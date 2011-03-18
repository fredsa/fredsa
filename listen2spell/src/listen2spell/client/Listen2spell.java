package listen2spell.client;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.voices.client.FlashSound;
import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

import listen2spell.shared.Word;

public class Listen2spell implements EntryPoint {
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

  protected void show(Word w) {
    String word = w.getWord();
    SoundController sc = new SoundController();
    sc.setPreferredSoundType(FlashSound.class);
    String url = w.getUrl();
    RootPanel.get().add(new HTML(word));
    RootPanel.get().add(
        new HTML(word + " (<audio controls preload src='" + url + "'>" + url + "</audio>)"));
    Sound sound = sc.createSound("audio/mime", url);
    sound.play();
  }

  private String makeUrl(String word) {
    return word;
    //    return "http://translate.google.com/translate_tts?ie=UTF-8&tl=en&prev=input&q="
    //        + URL.encodeQueryString(word);
  }
}
