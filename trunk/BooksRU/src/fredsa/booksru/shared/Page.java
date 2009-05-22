package fredsa.booksru.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Page implements Serializable {
  private ArrayList<Line> lines = new ArrayList<Line>();

  private HashMap<Line, Line[]> suggestionMap = new HashMap<Line, Line[]>();

  //  private PageView pageEventListener;

  public Page() {
  }

  //  public void addLine() {
  //    int size = lines.size();
  //    Line line = size == 0 ? Line.NULL_LINE : new Line(lines.get(size - 1), "");
  //    addLine(line);
  //  }
  //
  //  public void setPageEventListener(PageView pageEventListener) {
  //    this.pageEventListener = pageEventListener;
  //  }
  //
  //  private void addLine(Line line) {
  //    lines.add(line);
  //    pageEventListener.onLineAdded(line);
  //  }

  public void setSuggestions(Line previousLine, Line[] suggestedLines) {
    suggestionMap.put(previousLine, suggestedLines);
  }

}
