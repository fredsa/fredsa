package fredsa.booksru.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;

import fredsa.booksru.client.BookCoverWidget;
import fredsa.booksru.shared.Book;

import java.util.List;

public class BookGridView extends Composite implements HasSelectionHandlers<Book> {
  private final FlexTable grid = new FlexTable();

  public BookGridView() {
    initWidget(grid);
  }

  public HandlerRegistration addSelectionHandler(SelectionHandler<Book> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  public void setBooks(List<Book> books) {
    int count = books.size();
    int cols = (int) Math.ceil(Math.sqrt(count));
    //    int rows = (int) Math.ceil(count / cols);
    grid.clear();
    for (int i = 0; i < count; i++) {
      int row = i / cols;
      int col = i % cols;
      final BookCoverWidget bookCover = new BookCoverWidget();
      bookCover.setBook(books.get(i));
      grid.setWidget(row, col, bookCover);
      bookCover.addClickHandler(new ClickHandler() {

        public void onClick(ClickEvent event) {
          SelectionEvent.fire(BookGridView.this, bookCover.getBook());
        }
      });
    }
  }
}
