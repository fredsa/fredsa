package fredsa.booksru.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

import com.allen_sauer.gwt.log.client.Log;

import fredsa.booksru.shared.Book;

import java.util.List;

/*
 * * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BooksRU implements EntryPoint {

  private final BookServiceAsync bookService = GWT.create(BookService.class);

  public void onModuleLoad() {
    Log.setUncaughtExceptionHandler();

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        onModuleLoad2();
      }
    });
  }

  private void onModuleLoad2() {
    RootPanel rootPanel = RootPanel.get();

    final BookGrid bookResultsGrid = new BookGrid();
    rootPanel.add(bookResultsGrid);

    bookService.getBooks(new AsyncCallback<List<Book>>() {

      public void onFailure(Throwable caught) {
        Log.warn("Failed to retrieve books", caught);
      }

      public void onSuccess(List<Book> result) {
        bookResultsGrid.setBooks(result);
      }
    });

    //    BookCoverWidget book1 = new BookCoverWidget();
    //    book1.setTitle("Why did the chicken cross the road?");
    //
    //    BookCoverWidget book2 = new BookCoverWidget();
    //    book2.setTitle("Short Stories");
    //
    //    rootPanel.add(book1);
    //    rootPanel.add(book2);
  }
}
