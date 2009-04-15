package fredsa.booksru.server;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import fredsa.booksru.client.BookService;
import fredsa.booksru.shared.Book;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("serial")
public class BookServiceImpl extends RemoteServiceServlet implements BookService {

  private static final String TITLE = "title";
  private static final String AUTHOR = "author";
  private static final String BOOK = "Book";

  public List<Book> getBooks() {
    //PersistenceManager pm = PMF.get().getPersistenceManager();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query(BOOK);
    List<Entity> results = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(10));

    List<Book> books = new ArrayList<Book>();

    if (results.isEmpty()) {
      Book book;

      book = new Book();
      book.setTitle("Why did the chicken cross the road?");
      book.setAuthor("Ford");
      putBook(datastore, book);

      book = new Book();
      book.setTitle("Short, Scary Stories");
      book.setAuthor("Trisha");
      putBook(datastore, book);
    } else {
      for (Iterator<Entity> iterator = results.iterator(); iterator.hasNext();) {
        Entity entity = iterator.next();
        Book book = new Book();
        book.setAuthor((String) entity.getProperty(AUTHOR));
        book.setTitle((String) entity.getProperty(TITLE));
        books.add(book);
      }
    }

    return books;
  }

  private void putBook(DatastoreService datastore, Book book) {
    Entity entity = new Entity(BOOK);
    entity.setProperty(AUTHOR, book.getAuthor());
    entity.setProperty(TITLE, book.getTitle());
    datastore.put(entity);
  }
}
