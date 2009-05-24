package fredsa.booksru.client.view;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import fredsa.booksru.client.LineSuggestOracle;
import fredsa.booksru.shared.Line;

public class LineView extends Composite implements HasValueChangeHandlers<String> {

  private LineSuggestOracle oracle = new LineSuggestOracle();
  private SuggestBox suggestBox;

  public LineView() {
    suggestBox = new SuggestBox(oracle);
    suggestBox.addStyleName("lineview");

    suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {
      public void onSelection(SelectionEvent<Suggestion> event) {
        ValueChangeEvent.fire(LineView.this, event.getSelectedItem().getReplacementString());
      }
    });

    // Workaround for GWT issue 3533
    suggestBox.getTextBox().addKeyUpHandler(new KeyUpHandler() {
      public void onKeyUp(KeyUpEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_BACKSPACE
            && suggestBox.getText().length() == 0) {
          ValueChangeEvent.fire(LineView.this, null);
        }
      }
    });

    initWidget(suggestBox);
  }

  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  public void clear() {
    oracle.setSuggestions(null);
    suggestBox.setText("");
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    suggestBox.setFocus(true);
  }

  public void setSuggestions(Line[] suggestedLines) {
    oracle.setSuggestions(suggestedLines);
    suggestBox.showSuggestionList();
  }
}
