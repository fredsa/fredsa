package listen2spell.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

public class Challenge extends Composite {
  private static final String SPACES = "---------------------------------------";
  private String answer;
  private FlowPanel container;
  private TextBox hiddenTextBox;
  private HTML label;
  private Speaker speaker;
  private final String word;

  public Challenge(final String word) {
    this.word = word;
    container = new FlowPanel();
    hiddenTextBox = new TextBox();
    label = new HTML();
    label.setStylePrimaryName("answerbox");
    container.add(label);
    container.add(hiddenTextBox);
    initWidget(container);

    hiddenTextBox.addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
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

    });

    hiddenTextBox.addKeyPressHandler(new KeyPressHandler() {
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
    });
    setText("");
  }

  public void setSpeaker(Speaker speaker) {
    this.speaker = speaker;
  }

  @Override
  protected void onLoad() {
    hiddenTextBox.setFocus(true);
  }

  protected String pad(String txt) {
    return txt + trim(SPACES, word.length() - txt.length());
  }

  private void setText(String txt) {
    // allow up to 2x the word length
    answer = trim(txt, word.length() * 2);
    if (word.equals(txt)) {
      speaker.speak(txt);
    }
    label.setHTML(pad(txt));
  }

  private String trim(String txt, int len) {
    len = Math.max(len, 0);
    return txt.length() > len ? txt.substring(0, len) : txt;
  }
}
