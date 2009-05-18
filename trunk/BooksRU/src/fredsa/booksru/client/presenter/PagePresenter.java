package fredsa.booksru.client.presenter;

import com.google.gwt.core.client.GWT;

import fredsa.booksru.client.BookService;
import fredsa.booksru.client.BookServiceAsync;
import fredsa.booksru.client.ValueAddEvent;
import fredsa.booksru.client.ValueAddHandler;
import fredsa.booksru.client.view.PageView;
import fredsa.booksru.shared.Page;

public class PagePresenter {

  private final BookServiceAsync bookService = GWT.create(BookService.class);

  private final Page page;

  private final PageView pageView;

  public PagePresenter(PageView pageView, Page page) {
    this.pageView = pageView;
    this.page = page;
    pageView.setPage(page);
    pageView.addValueAddHandler(new ValueAddHandler<String>() {

      public void onValueAdd(ValueAddEvent<String> event) {
        addLine();
      }
    });
    addLine();
  }

  private void addLine() {
    page.addLine();
  }
}
