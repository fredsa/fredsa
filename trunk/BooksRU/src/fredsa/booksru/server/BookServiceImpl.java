package fredsa.booksru.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fredsa.booksru.client.BookService;
import fredsa.booksru.shared.Book;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class BookServiceImpl extends RemoteServiceServlet implements BookService {

  public List<Book> getBooks() {
    List<Book> books = new ArrayList<Book>();
    Book book;

    book = new Book();
    book.setTitle("Why did the chicken cross the road?");
    book.setAuthor("Ford");
    books.add(book);

    book = new Book();
    book.setTitle("Short, Scary Stories");
    book.setAuthor("Trisha");
    books.add(book);

    return books;
  }
}
