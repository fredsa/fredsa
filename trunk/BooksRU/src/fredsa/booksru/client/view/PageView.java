package fredsa.booksru.client.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import com.allen_sauer.gwt.log.client.Log;

import fredsa.booksru.shared.Book;

import java.util.Date;
import java.util.List;

public class PageView extends FlowPanel {

  private final BookGridView bookResultsGrid;

  public PageView() {
    add(new Label(new Date().toString()));

    bookResultsGrid = new BookGridView();
    add(bookResultsGrid);

  }

  public void setBooks(List<Book> result) {
    bookResultsGrid.setBooks(result);
  }

  public void showError(Throwable caught, String message) {
    Log.warn(message, caught);
  }

}