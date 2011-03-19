package listen2spell.client;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.voices.client.Html5Sound;
import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.HashMap;

import listen2spell.shared.Word;

public class Speaker {
  private SoundController sc;

  private HashMap<String, Sound> soundMap = new HashMap<String, Sound>();

  private WordServiceAsync wordService;

  @SuppressWarnings("deprecation")
  public Speaker() {
    sc = new SoundController();
    sc.setPreferredSoundType(Html5Sound.class);
  }

  public void setService(WordServiceAsync wordService) {
    this.wordService = wordService;
  }

  public void speak(String word) {
    Sound sound = soundMap.get(word);
    if (sound != null) {
      sound.play();
      return;
    }
    wordService.getWords(word, new AsyncCallback<Word[]>() {
      public void onFailure(Throwable e) {
        Log.warn("Unable to get word", e);
      }

      public void onSuccess(Word[] result) {
        String url = result[0].getUrl();
        Sound sound = sc.createSound("audio/mpeg", url);
        sound.play();
      }
    });
  }
}
