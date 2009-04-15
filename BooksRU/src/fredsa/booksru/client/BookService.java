package fredsa.booksru.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import fredsa.booksru.shared.Book;

import java.util.List;

/*
 * * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface BookService extends RemoteService {
  List<Book> getBooks();
}
