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
  protected String html ="";
  private FocusPanel focusPanel;
  private HTML label;

  public Challenge(String word) {
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
            html = html.substring(0, html.length() - 1);
            label.setHTML(html);
          }
        }
      }
    });

    focusPanel.addKeyPressHandler(new KeyPressHandler() {
      public void onKeyPress(KeyPressEvent event) {
        html += event.getCharCode();
        label.setHTML(html);
      }
    });
  }

  @Override
  protected void onLoad() {
    focusPanel.setFocus(true);
  }
}
