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
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import fredsa.booksru.shared.Line;

public class LineWidget extends Composite implements HasValueChangeHandlers<String> {

  private MySuggestBox suggestBox;

  public LineWidget(Line previousLine) {

    LineSuggestOracle oracle = new LineSuggestOracle(previousLine);
    suggestBox = new MySuggestBox(oracle);
    suggestBox.addStyleName("line-widget");

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

    initWidget(suggestBox);
  }

  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  public void setEditable(boolean editable) {
    suggestBox.setEditable(editable);
  }

  @Override
  protected void onLoad() {
    super.onLoad();
  }
}
