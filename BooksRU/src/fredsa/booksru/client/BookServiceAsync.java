package fredsa.booksru.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import fredsa.booksru.shared.Book;

import java.util.List;

public interface BookServiceAsync {
  void getBooks(AsyncCallback<List<Book>> callback);
}
