package fredsa.booksru.client;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fredsa.booksru.shared.Book;
import fredsa.booksru.shared.Line;

public interface BookServiceAsync {
  void getBooks(AsyncCallback<List<Book>> callback);

  void getLineSuggestions(Line previousLine, AsyncCallback<Line[]> callback);
}
