package fredsa.booksru.shared;

import fredsa.booksru.client.view.PageView;

import java.io.Serializable;
import java.util.ArrayList;

public class Page implements Serializable {
  private ArrayList<Line> lines = new ArrayList<Line>();

  private PageView pageEventListener;

  public Page() {
  }

  public void addLine() {
    int size = lines.size();
    Line line = size == 0 ? Line.NULL_LINE : new Line(lines.get(size - 1), "");
    addLine(line);
  }

  public void setPageEventListener(PageView pageEventListener) {
    this.pageEventListener = pageEventListener;
  }

  private void addLine(Line line) {
    lines.add(line);
    pageEventListener.onLineAdded(line);
  }

}
