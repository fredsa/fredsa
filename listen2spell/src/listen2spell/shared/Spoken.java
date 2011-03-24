package listen2spell.shared;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Spoken implements Serializable {
  public static final Spoken EMPTY = new Spoken("", "data:", new byte[] {});
  private byte[] bytes;
  private String url;
  private String word;

  public Spoken(String word, String url, byte[] bytes) {
    this.word = word;
    this.url = url;
    this.bytes = bytes;
  }

  @SuppressWarnings("unused")
  private Spoken() {
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
