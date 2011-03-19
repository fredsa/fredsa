package listen2spell.client;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.voices.client.Html5Sound;
import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.allen_sauer.gwt.voices.client.handler.PlaybackCompleteEvent;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;
import com.allen_sauer.gwt.voices.client.handler.SoundLoadStateChangeEvent;

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

  public void speak(final String word) {
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
        sound.addEventHandler(new SoundHandler() {

          public void onPlaybackComplete(PlaybackCompleteEvent event) {
            Log.debug("complete");
          }

          public void onSoundLoadStateChange(SoundLoadStateChangeEvent event) {
            Log.debug("" + event.getLoadStateAsString());
          }
        });
        soundMap.put(word, sound);
        sound.play();
      }
    });
  }
}
