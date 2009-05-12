package fredsa.booksru.client;

import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

public class MySuggestBox extends DeckPanel implements HasValueChangeHandlers<String> {
  private static final int WIDGET_DISABLED = 1;
  private static final int WIDGET_ENABLED = 0;

  private Label label;

  private SuggestBox suggestBox;

  public MySuggestBox(SuggestOracle oracle) {
    // suggest box widget for editing
    suggestBox = new SuggestBox(oracle);
    suggestBox.addStyleName("line-widget");

    // label widget for viewing
    label = new Label();

    add(suggestBox);
    add(label);

    setEditable(false);
  }

  public void setEditable(boolean editable) {
    showWidget(editable ? WIDGET_ENABLED : WIDGET_DISABLED);
    if (editable) {
      suggestBox.setFocus(true);
    }
  }

}
