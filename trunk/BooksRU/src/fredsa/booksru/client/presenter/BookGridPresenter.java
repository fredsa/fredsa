package fredsa.booksru.client.presenter;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;

import fredsa.booksru.client.view.BookGridView;
import fredsa.booksru.shared.Book;

public class BookGridPresenter {
  private final BookGridView<Book> bookGridView;

  public BookGridPresenter(final BookGridView<Book> bookGridView) {
    this.bookGridView = bookGridView;
    bookGridView.addSelectionHandler(new SelectionHandler<Book>() {
      public void onSelection(SelectionEvent<Book> event) {
        Window.alert("Selected " + event.getSelectedItem());
      }
    });
  }
}
