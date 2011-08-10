package listen2spell.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import listen2spell.client.Speaker;
import listen2spell.shared.Util;

public class ChallengeWidget extends Composite {

  interface ChallengeWidgetUiBinder extends UiBinder<Widget, ChallengeWidget> {
  }

  private static ChallengeWidgetUiBinder uiBinder = GWT.create(ChallengeWidgetUiBinder.class);

  @UiField
  HTMLPanel container;

  @UiField
  TextBox hiddenTextBox;

  @UiField
  HTML label;

  private String answer;

  private Speaker speaker;

  private final String word;

  public ChallengeWidget(final String word) {
    initWidget(uiBinder.createAndBindUi(this));
    this.word = word;
    setText("");
  }

  @UiHandler(value = "hiddenTextBox")
  public void onKeyPress(KeyPressEvent event) {
    if (!event.isAltKeyDown() && !event.isControlKeyDown() && !event.isMetaKeyDown()) {
      event.preventDefault();
      char code = event.getCharCode();
      if (code >= 'a' && code <= 'z' || code >= 'A' && code <= 'Z') {
        speaker.speak("" + code);
        setText(answer + code);
      }
    }
  }

  public void setSpeaker(Speaker speaker) {
    this.speaker = speaker;
  }

  @Override
  protected void onLoad() {
    hiddenTextBox.setFocus(true);
  }

  @UiHandler(value = "hiddenTextBox")
  void onKeyDown(KeyDownEvent event) {
    if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
      event.preventDefault();
      if (answer.length() > 0) {
        setText(answer.substring(0, answer.length() - 1));
      }
    } else if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
      event.preventDefault();
      speaker.speak(answer);
    } else if (event.getNativeKeyCode() == ' ') {
      event.preventDefault();
      speaker.speak(word);
    }
  }

  private void setText(String txt) {
    // allow up to 2x the word length
    answer = Util.trim(txt, word.length() * 2);
    if (word.equals(txt)) {
      speaker.speak(txt);
    }
    label.setHTML(Util.rightPadDashes(txt, word.length()));
  }

}
