package fredsa.booksru.client;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

import fredsa.booksru.shared.Line;

public class LineWidget extends Composite {

  private SuggestBox suggestBox;

  public LineWidget(Line previousLine) {
    LineSuggestOracle oracle = new LineSuggestOracle(previousLine);
    suggestBox = new SuggestBox(oracle);
    suggestBox.addStyleName("line-widget");
    initWidget(suggestBox);
    suggestBox.addSelectionHandler(new SelectionHandler<Suggestion>() {

      public void onSelection(SelectionEvent<Suggestion> event) {
        Window.alert("foo");
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

  @Override
  protected void onLoad() {
    super.onLoad();
    suggestBox.setFocus(true);
  }
}
