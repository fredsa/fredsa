package fredsa.booksru.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class MySuggestBox extends DeckPanel implements HasSelectionHandlers<String> {
  private static final int WIDGET_DISABLED = 1;
  private static final int WIDGET_ENABLED = 0;

  private Label label;

  private SuggestBox suggestBox;

  public MySuggestBox(SuggestOracle oracle) {
    setStylePrimaryName("my-suggest-box");

    // suggest box widget for editing
    suggestBox = new SuggestBox(oracle);
    suggestBox.addStyleName("line-widget");

    // label widget for viewing
    label = new Label("label");

    add(suggestBox);
    add(label);

    suggestBox.addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
          event.preventDefault();
          processNewText();
        }
      }
    });

    suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {
      public void onSelection(SelectionEvent<Suggestion> event) {
        processNewText();
      }

    });
    setEditable(false);
  }

  public HandlerRegistration addSelectionHandler(SelectionHandler<String> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  public void setEditable(boolean editable) {
    showWidget(editable ? WIDGET_ENABLED : WIDGET_DISABLED);
    if (editable) {
      suggestBox.setFocus(true);
    }
  }

  public void setFocus(boolean focused) {
    suggestBox.setFocus(focused);
  }

  private void processNewText() {
    label.setText(suggestBox.getValue());
    SelectionEvent.fire(MySuggestBox.this, suggestBox.getValue());
  }

}
