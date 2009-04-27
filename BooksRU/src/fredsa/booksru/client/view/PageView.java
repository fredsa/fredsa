package fredsa.booksru.client.view;

import java.util.Date;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import fredsa.booksru.shared.Book;

public class PageView extends FlowPanel {

  private final BookGridView<Book> bookResultsGrid;

  public PageView() {
    add(new Label(new Date().toString()));

    bookResultsGrid = new BookGridView<Book>();
    add(bookResultsGrid);

  }

  public void setBooks(List<Book> result) {
    bookResultsGrid.setBooks(result);
  }

  public void showError(Throwable caught, String message) {
    Log.warn(message, caught);
  }

}