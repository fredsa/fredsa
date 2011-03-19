package listen2spell.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;

public class Challenge extends Composite {
  private static final String SPACES = "---------------------------------------";
  private FocusPanel focusPanel;
  private String html;
  private HTML label;
  private Speaker speaker;
  private final String word;

  public Challenge(String word) {
    this.word = word;
    focusPanel = new FocusPanel();
    focusPanel.setStylePrimaryName("answerbox");
    label = new HTML();
    focusPanel.add(label);
    initWidget(focusPanel);

    focusPanel.addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE) {
          event.preventDefault();
          if (html.length() > 0) {
            setText(html.substring(0, html.length() - 1));
          }
        }
      }

    });

    focusPanel.addKeyPressHandler(new KeyPressHandler() {
      public void onKeyPress(KeyPressEvent event) {
        char code = event.getCharCode();
        if (code >= 'a' && code <= 'z' || code >= 'A' && code <= 'Z') {
          speaker.speak("" + code);
          setText(html + code);
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
    focusPanel.setFocus(true);
  }

  protected String pad(String txt) {
    return txt + trim(SPACES, word.length() - txt.length());
  }

  private void setText(String txt) {
    // allow up to 2x the word length
    html = trim(txt, word.length() * 2);
    label.setHTML(pad(txt));
  }

  private String trim(String txt, int len) {
    len = Math.max(len, 0);
    return txt.length() > len ? txt.substring(0, len) : txt;
  }
}
