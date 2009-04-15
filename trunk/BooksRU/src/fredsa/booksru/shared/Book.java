package fredsa.booksru.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Book implements Serializable {
  String author;

  String title;

  public String getAuthor() {
    return author;
  }

  public String getTitle() {
    return title;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
