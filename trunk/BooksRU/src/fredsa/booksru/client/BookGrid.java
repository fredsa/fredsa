package fredsa.booksru.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;

import fredsa.booksru.shared.Book;

import java.util.List;

public class BookGrid extends Composite {
  public BookGrid() {
    initWidget(grid);
  }

  private final FlexTable grid = new FlexTable();

  public void setBooks(List<Book> books) {
    int count = books.size();
    int cols = (int) Math.ceil(Math.sqrt(count));
    //    int rows = (int) Math.ceil(count / cols);
    grid.clear();
    for (int i = 0; i < count; i++) {
      int row = i / cols;
      int col = i % cols;
      BookCoverWidget bookCover = new BookCoverWidget();
      bookCover.setBook(books.get(i));
      grid.setWidget(row, col, bookCover);
    }
  }
}
