package fredsa.booksru.client;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;

import fredsa.booksru.shared.Line;

public class LineWidget extends Composite implements HasValueChangeHandlers<String> {

  private MySuggestBox suggestBox;

  public LineWidget(Line previousLine) {
    LineSuggestOracle oracle = new LineSuggestOracle(previousLine);
    suggestBox = new MySuggestBox(oracle);
    suggestBox.addStyleName("line-widget");

    suggestBox.addSelectionHandler(new SelectionHandler<String>() {
      public void onSelection(SelectionEvent<String> event) {
        ValueChangeEvent.fire(LineWidget.this, event.getSelectedItem());
      }
    });

    initWidget(suggestBox);
  }

  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  public void setEditable(boolean editable) {
    suggestBox.setEditable(editable);
    if (editable) {
      suggestBox.setFocus(true);
    }
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    // start in editing mode
    setEditable(true);
  }
}
