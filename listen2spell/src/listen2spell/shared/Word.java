package listen2spell.shared;

import java.io.Serializable;

public class Word implements Serializable {
  private String url;
  private String word;

  public Word(String word, String url) {
    this.word = word;
    this.url = url;
  }

  private Word() {
  }

  public String getUrl() {
    return url;
  }

  public String getWord() {
    return word;
  }
}
