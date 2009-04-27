package fredsa.booksru.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

import fredsa.booksru.shared.Book;

public class BookCoverWidget<T extends Book> extends FocusPanel {

  private final Label authorLabel = new Label();
  private T book;
  private final FlowPanel flowPanel = new FlowPanel();
  private final Label titleLabel = new Label();

  public BookCoverWidget() {
    addStyleName("book-cover");
    new MouseHandlerDecorator(this);
    new FocusHandlerDecorator(this);

    setWidget(flowPanel);

    titleLabel.setStylePrimaryName("book-title");
    flowPanel.add(titleLabel);

    authorLabel.setStylePrimaryName("book-author");
    flowPanel.add(authorLabel);
  }

  public T getBook() {
    return book;
  }

  public void setBook(T book) {
    this.book = book;
    titleLabel.setText(book.getTitle());
    authorLabel.setText(book.getAuthor());
  }
}
