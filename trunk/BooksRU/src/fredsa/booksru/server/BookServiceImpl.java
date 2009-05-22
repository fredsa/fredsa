package fredsa.booksru.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fredsa.booksru.client.BookService;
import fredsa.booksru.shared.Book;
import fredsa.booksru.shared.Line;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

@SuppressWarnings("serial")
public class BookServiceImpl extends RemoteServiceServlet implements BookService {

  public List<Book> getBooks() {
    List<Book> books;
    PersistenceManager pm = PMF.get().getPersistenceManager();

    books = getBooks(pm);

    if (books.isEmpty()) {
      Book book;

      book = new Book();
      book.setTitle("Why did the chicken cross the road?");
      book.setAuthor("Ford");
      pm.makeNontransactional(book);

      book = new Book();
      book.setTitle("Short, Scary Stories");
      book.setAuthor("Trisha");
      pm.makeNontransactional(book);
    }

    return books;
  }

  public Line[] getLineSuggestions(Line previousLine) {
    Line line1 = new Line(previousLine, "Once upon a time");
    Line line2 = new Line(previousLine, "It was a dark and stormy night");
    return new Line[] {line1, line2,};
  }

  @SuppressWarnings("unchecked")
  private List<Book> getBooks(PersistenceManager pm) {
    Query query = pm.newQuery(Book.class);
    query.setRange(0, 10);
    return new ArrayList<Book>(((List<Book>) query.execute()));
  }
}