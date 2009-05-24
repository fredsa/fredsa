package fredsa.booksru.shared;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

// COMPOUND KEY
// - preceedingLine = 2 / there was a prince
// - scattertime
// - line = [3] / and a princess
// 
// VALUES
// - reads = 100
// - rank = 3
// - backtrackLine = [1] / Once upon a time

@SuppressWarnings("serial")
@PersistenceCapable
public class Line implements Serializable {
  private static final String DELIM = "/";

  private static final int LINE_NUMBER = 4;

  private static final int LINE_TEXT = 5;

  public static final Line NULL_LINE;
  private static final int PRECEEDING_LINE_NUMBER = 1;

  private static final int PRECEEDING_LINE_TEXT = 2;

  private static final int SCATTER_TIME = 3;

  static {
    NULL_LINE = new Line(null, "");
    NULL_LINE.setReads(1);
  }

  private static String escapeText(String text) {
    return text.replaceAll("/", "");
  }

  private static long reverse(long scatterTime) {
    return Long.reverse(scatterTime);
  }

  @Persistent
  private int backtrackLineNumber;

  @Persistent
  private String backtrackLineText;

  @Persistent
  @PrimaryKey
  private String compoundKey = DELIM + DELIM + DELIM + DELIM + DELIM;

  @Persistent
  private int rank;

  @Persistent
  private int reads;

  protected Line() {
  }

  public Line(Line previousLine, String lineText) {
    if (previousLine == null) {
      setPreceedingLineNumber(-1);
      setPreceedingLineText("");
    } else {
      setPreceedingLineNumber(previousLine.getLineNumber());
      setPreceedingLineText(previousLine.getLineText());
    }
    setLineNumber(getPreceedingLineNumber() + 1);
    setLineText(escapeText(lineText));
    setReads(0);
    setRank(0);
    setScatterTime(System.currentTimeMillis());
    if (previousLine == null) {
      setBacktrackLineNumber(-2);
      setBacktrackLineText("");
    } else {
      setBacktrackLineNumber(previousLine.getPreceedingLineNumber());
      setBacktrackLineText(previousLine.getPreceedingLineText());
    }
  }

  private String concat(String[] a) {
    String t = a[0];
    for (int i = 1; i < a.length; i++) {
      t += DELIM + a[i];
    }
    return t;
  }

  public int getBacktrackLineNumber() {
    return backtrackLineNumber;
  }

  public String getBacktrackLineText() {
    return backtrackLineText;
  }

  public String getCompoundKey() {
    return compoundKey;
  }

  private String[] getKeyAsArray() {
    return (compoundKey + DELIM).split(DELIM, 7);
  }

  private String getKeyElement(int i) {
    String a[] = getKeyAsArray();
    return a[i];
  }

  public int getLineNumber() {
    return Integer.parseInt(getKeyElement(LINE_NUMBER));
  }

  public String getLineText() {
    return getKeyElement(LINE_TEXT);
  }

  public int getPreceedingLineNumber() {
    return Integer.parseInt(getKeyElement(PRECEEDING_LINE_NUMBER));
  }

  public String getPreceedingLineText() {
    return getKeyElement(PRECEEDING_LINE_TEXT);
  }

  public String getPrefixFilterForNextLine() {
    return DELIM + getLineNumber() + DELIM + getLineText() + DELIM;
  }

  public int getRank() {
    return rank;
  }

  public int getReads() {
    return reads;
  }

  public long getScatterTime() {
    return reverse(Long.parseLong(getKeyElement(SCATTER_TIME)));
  }

  public void incrementReads() {
    setReads(getReads() + 1);
    setRank(getReads() / 2);
  }

  public void setBacktrackLineNumber(int backtrackLineNumber) {
    this.backtrackLineNumber = backtrackLineNumber;
  }

  public void setBacktrackLineText(String backtrackLineText) {
    this.backtrackLineText = backtrackLineText;
  }

  public void setCompoundKey(String compoundKey) {
    this.compoundKey = compoundKey;
  }

  private void setKeyElement(int i, String value) {
    String a[] = getKeyAsArray();
    a[i] = value;
    compoundKey = concat(a);
  }

  public void setLineNumber(int lineNumber) {
    setKeyElement(LINE_NUMBER, Integer.toString(lineNumber));
  }

  public void setLineText(String lineText) {
    setKeyElement(LINE_TEXT, lineText);
  }

  public void setPreceedingLineNumber(int preceedingLineNumber) {
    setKeyElement(PRECEEDING_LINE_NUMBER, Integer.toString(preceedingLineNumber));
  }

  public void setPreceedingLineText(String preceedingLineText) {
    setKeyElement(PRECEEDING_LINE_TEXT, preceedingLineText);
  }

  private void setRank(int rank) {
    this.rank = rank;
  }

  private void setReads(int reads) {
    this.reads = reads;
  }

  public void setScatterTime(long scatterTime) {
    setKeyElement(SCATTER_TIME, Long.toString(reverse(scatterTime)));
  }

  @Override
  public String toString() {
    return getCompoundKey() + ";reads=" + getReads() + ";rank=" + getRank();
  }
}
