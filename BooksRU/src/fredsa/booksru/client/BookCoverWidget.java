package fredsa.booksru.client;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;

import fredsa.booksru.shared.Book;

public class BookCoverWidget extends FocusPanel {

  private final Label authorLabel = new Label();
  private Book book;
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

  public Book getBook() {
    return book;
  }

  public void setBook(Book book) {
    this.book = book;
    titleLabel.setText(book.getTitle());
    authorLabel.setText(book.getAuthor());
  }
}
