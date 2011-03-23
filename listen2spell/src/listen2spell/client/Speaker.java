package listen2spell.client;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.voices.client.FlashSound;
import com.allen_sauer.gwt.voices.client.Html5Sound;
import com.allen_sauer.gwt.voices.client.Sound;
import com.allen_sauer.gwt.voices.client.SoundController;
import com.allen_sauer.gwt.voices.client.handler.PlaybackCompleteEvent;
import com.allen_sauer.gwt.voices.client.handler.SoundHandler;
import com.allen_sauer.gwt.voices.client.handler.SoundLoadStateChangeEvent;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import listen2spell.shared.Word;

public class Speaker {
  private SoundController sc;

  private HashMap<String, Sound> soundMap = new HashMap<String, Sound>();
  private boolean speaking;

  private List<String> todo = new ArrayList<String>();
  private WordServiceAsync wordService;

  @SuppressWarnings("deprecation")
  public Speaker() {
    sc = new SoundController();
    sc.setPreferredSoundType(Html5Sound.class);
    sc.setPreferredSoundType(FlashSound.class);
  }

  public void setService(WordServiceAsync wordService) {
    this.wordService = wordService;
  }

  public void speak(final String word) {
    todo.add(word);
    Sound sound = soundMap.get(word);
    if (sound != null) {
      maybePlay();
      return;
    }
    wordService.getSpokenWord(word, new AsyncCallback<Word>() {

      @Override
      public void onFailure(Throwable e) {
        Log.warn("Unable to get word", e);
      }

      @Override
      public void onSuccess(Word w) {
        String url = w.getUrl();
        Sound sound = sc.createSound("audio/mpeg", url);
        Log.debug("sound: " + sound);
        sound.addEventHandler(new SoundHandler() {
          public void onPlaybackComplete(PlaybackCompleteEvent event) {
            Log.debug("onPlaybackComplete: " + event);
            speaking = false;
            maybePlay();
          }

          public void onSoundLoadStateChange(SoundLoadStateChangeEvent event) {
            Log.debug("onSoundLoadStateChange: " + event.getLoadStateAsString());
          }
        });
        soundMap.put(word, sound);
        maybePlay();
      }
    });
  }

  protected void maybePlay() {
    if (speaking) {
      return;
    }
    if (todo.size() == 0) {
      return;
    }

    String word = todo.get(0);
    Sound sound = soundMap.get(word);
    if (sound != null) {
      todo.remove(0);
      speaking = true;
      sound.play();
    }
  }
}
