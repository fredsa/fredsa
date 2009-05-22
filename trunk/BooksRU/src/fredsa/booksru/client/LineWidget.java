package fredsa.booksru.client;

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

  private LineSuggestOracle oracle = new LineSuggestOracle();
  private MySuggestBox suggestBox;

  public LineWidget() {
    suggestBox = new MySuggestBox(oracle);
    suggestBox.addStyleName("line-widget");

    suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {
      public void onSelection(SelectionEvent<Suggestion> event) {
        ValueChangeEvent.fire(LineWidget.this, event.getSelectedItem().getReplacementString());
      }
    });

    initWidget(suggestBox);
  }

  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  public void setSuggestions(Line[] suggestedLines) {
    oracle.setSuggestions(suggestedLines);

  }

  public void setText(String text) {
    suggestBox.setText(text);
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    suggestBox.setFocus(true);
  }
}
