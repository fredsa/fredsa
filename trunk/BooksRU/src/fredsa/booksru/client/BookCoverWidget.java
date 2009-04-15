package fredsa.booksru.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import fredsa.booksru.shared.Book;

public class BookCoverWidget extends Composite {

  private final Label titleLabel = new Label();
  private final Label authorLabel = new Label();
  private final FlowPanel container = new FlowPanel();
  private Book book;

  public Book getBook() {
    return book;
  }

  public BookCoverWidget() {
    initWidget(container);
    addStyleName("book-cover");

    titleLabel.setStylePrimaryName("book-title");
    container.add(titleLabel);

    authorLabel.setStylePrimaryName("book-author");
    container.add(authorLabel);
  }

  public void setBook(Book book) {
    this.book = book;
    titleLabel.setText(book.getTitle());
    authorLabel.setText(book.getAuthor());
  }
}
