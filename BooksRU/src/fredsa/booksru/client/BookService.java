package fredsa.booksru.client;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import fredsa.booksru.shared.Book;
import fredsa.booksru.shared.Line;

/*
 * * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface BookService extends RemoteService {
  List<Book> getBooks();

  Line[] getLineSuggestions(Line previousLine);
}
