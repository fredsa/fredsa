package fredsa.booksru.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Page implements Serializable {
  private ArrayList<Line> lines = new ArrayList<Line>();

  private HashMap<Line, Line[]> suggestionMap = new HashMap<Line, Line[]>();

  public Page() {
  }

  public void addLine(Line line) {
    lines.add(line);
  }

  public Line getLastLine() {
    return lines.isEmpty() ? Line.NULL_LINE : lines.get(lines.size() - 1);
  }

  public Line[] getSuggestions(Line previousLine) {
    return suggestionMap.get(previousLine);
  }

  public Line removeLastLine() {
    return lines.isEmpty() ? null : lines.remove(lines.size() - 1);
  }

  public void setSuggestions(Line previousLine, Line[] suggestedLines) {
    suggestionMap.put(previousLine, suggestedLines);
  }

}
