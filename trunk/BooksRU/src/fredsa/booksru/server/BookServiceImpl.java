package fredsa.booksru.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.allen_sauer.gwt.log.client.Log;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fredsa.booksru.client.BookService;
import fredsa.booksru.shared.Book;
import fredsa.booksru.shared.Line;

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

  @SuppressWarnings("unchecked")
  private List<Book> getBooks(PersistenceManager pm) {
    Query query = pm.newQuery(Book.class);
    query.setRange(0, 10);
    return new ArrayList<Book>(((List<Book>) query.execute()));
  }

  private List<Line> getLines(PersistenceManager pm, Line previousLine) {
    Query query = pm.newQuery(Line.class);
    query.setFilter("previousLineNumber == " + previousLine.getLineNumber());
    query.setRange(0, 10);
    List<Line> results = (List<Line>) query.execute();
    for (Iterator iterator = results.iterator(); iterator.hasNext();) {
      Line line = (Line) iterator.next();
      Log.debug(line.getCompoundKey());
    }
    return new ArrayList<Line>(results);
  }

  public Line[] getLineSuggestions(Line previousLine) {
    PersistenceManager pm = PMF.get().getPersistenceManager();
    if (previousLine.getLineNumber() > 0) {
      if (previousLine.getReads() == 0) {
        pm.makePersistent(previousLine);
      } else {
        Key key = KeyFactory.createKey(Line.class.getSimpleName(), previousLine.getCompoundKey());
        Line line = pm.getObjectById(Line.class, key);
        line.incrementReads();
        pm.makePersistent(line);
      }
    }
    return getLines(pm, previousLine).toArray(new Line[] {});
  }
}
///0//-4129273751919394816/1//////
