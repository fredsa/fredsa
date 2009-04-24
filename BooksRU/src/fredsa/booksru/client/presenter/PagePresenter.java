package fredsa.booksru.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import fredsa.booksru.client.BookService;
import fredsa.booksru.client.BookServiceAsync;
import fredsa.booksru.client.view.PageView;
import fredsa.booksru.shared.Book;

import java.util.List;

public class PagePresenter {

  private final BookServiceAsync bookService = GWT.create(BookService.class);
  private final PageView pageView;

  public PagePresenter(PageView pageView) {
    this.pageView = pageView;
  }

  public void start() {
    bookService.getBooks(new AsyncCallback<List<Book>>() {
      public void onFailure(Throwable caught) {
        pageView.showError(caught, "Failed to retrieve books");
      }

      public void onSuccess(List<Book> result) {
        pageView.setBooks(result);
      }
    });
  }

}
