package listen2spell.client.ui;

import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import listen2spell.client.Challenge;
import listen2spell.client.Speaker;
import listen2spell.client.WordServiceAsync;

public class GameWidget extends Composite {

  interface GameWidgetUiBinder extends UiBinder<Widget, GameWidget> {
  }

  private static GameWidgetUiBinder uiBinder = GWT.create(GameWidgetUiBinder.class);

  @UiField
  SimplePanel challengePanel;

  @UiField
  SimplePanel loggerPanel;

  @UiField
  SimplePanel wordListPanel;

  private Speaker speaker;

  public GameWidget(WordServiceAsync wordService) {
    initWidget(uiBinder.createAndBindUi(this));

    loggerPanel.add(Log.getLogger(DivLogger.class).getWidget());

    speaker = new Speaker();
    speaker.setService(wordService);

    wordService.getWordList(new AsyncCallback<String[]>() {
      public void onFailure(Throwable e) {
        Log.error("failed to get words from server", e);
      }

      public void onSuccess(String[] words) {
        wordListPanel.add(new WordListWidget(words));
        show(words[0]);
      }
    });
  }

  protected void show(String word) {
    RootPanel.get().setStylePrimaryName("answer");
    Challenge challenge = new Challenge(word);
    challenge.setSpeaker(speaker);
    RootPanel.get().add(challenge);
    speaker.speak(word);
  }

}
