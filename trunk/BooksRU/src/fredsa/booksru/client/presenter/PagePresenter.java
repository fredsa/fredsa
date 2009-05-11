package fredsa.booksru.client.presenter;

import com.google.gwt.core.client.GWT;

import fredsa.booksru.client.BookService;
import fredsa.booksru.client.BookServiceAsync;
import fredsa.booksru.client.ValueAddEvent;
import fredsa.booksru.client.ValueAddHandler;
import fredsa.booksru.client.view.PageView;
import fredsa.booksru.shared.Line;

public class PagePresenter {

  private final BookServiceAsync bookService = GWT.create(BookService.class);

  private final PageView pageView;

  public PagePresenter(PageView pageView) {
    this.pageView = pageView;
    pageView.addValueAddHandler(new ValueAddHandler<String>() {

      public void onValueAdd(ValueAddEvent<String> event) {
        addLine();
      }
    });
    addLine();
  }

  private void addLine() {
    pageView.setCurrentLineEditable(false);
    pageView.addLine(Line.NULL_LINE);
  }
}
