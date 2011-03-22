package listen2spell.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Word implements Serializable {
  private byte[] bytes;
  private String url;
  private String word;

  public Word(String word, String url, byte[] bytes) {
    this.word = word;
    this.url = url;
    this.bytes = bytes;
  }

  @SuppressWarnings("unused")
  private Word() {
  }

  public byte[] getData() {
    return bytes;
  }

  public String getUrl() {
    return url;
  }

  public String getWord() {
    return word;
  }
}
