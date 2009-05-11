package fredsa.booksru.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import fredsa.booksru.shared.Line;

public class LineWidget extends Composite implements HasValueChangeHandlers<String> {

  private static final int WIDGET_DISABLED = 1;

  private static final int WIDGET_ENABLED = 0;

  private final DeckPanel container = new DeckPanel();

  private SuggestBox suggestBox;

  public LineWidget(Line previousLine) {
    initWidget(container);

    // suggest box widget for editing
    LineSuggestOracle oracle = new LineSuggestOracle(previousLine);
    suggestBox = new SuggestBox(oracle);
    suggestBox.addStyleName("line-widget");

    // text box widget for viewing
    Label label = new Label();
    container.add(suggestBox);
    container.add(label);

    // start in editing mode
    setEditable(true);

    suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {
      public void onSelection(SelectionEvent<Suggestion> event) {
        Suggestion suggestion = event.getSelectedItem();
        suggestion.getReplacementString();
        ValueChangeEvent.fire(LineWidget.this, suggestion.getReplacementString());
      }
    });
    suggestBox.addKeyDownHandler(new KeyDownHandler() {

      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
          System.out.println("EnTER");
          event.preventDefault();
        }
      }
    });
  }

  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  public void setEditable(boolean editable) {
    container.showWidget(editable ? WIDGET_ENABLED : WIDGET_DISABLED);
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    suggestBox.setFocus(true);
  }
}
