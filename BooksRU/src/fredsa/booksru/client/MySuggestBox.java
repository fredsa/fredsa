package fredsa.booksru.client;

import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;

public class MySuggestBox extends SuggestBox {

  public MySuggestBox(SuggestOracle oracle) {
    super(oracle);

    setStylePrimaryName("my-suggest-box");

    //    addKeyDownHandler(new KeyDownHandler() {
    //      public void onKeyDown(KeyDownEvent event) {
    //        if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
    //          event.preventDefault();
    //          processNewText();
    //        }
    //      }
    //    });

    //    addSelectionHandler(new SelectionHandler<Suggestion>() {
    //      public void onSelection(SelectionEvent<Suggestion> event) {
    //        processNewText();
    //      }
    //
    //    });
  }

  private void processNewText() {
    //    SelectionEvent.fire(MySuggestBox.this, getValue());
  }

}
