package listen2spell.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Word implements Serializable {
  private String url;
  private String word;

  public Word(String word, String url) {
    this.word = word;
    this.url = url;
  }

  @SuppressWarnings("unused")
  private Word() {
  }

  public String getUrl() {
    return url;
  }

  public String getWord() {
    return word;
  }
}
