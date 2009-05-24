package fredsa.booksru.client;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.SuggestOracle;

import fredsa.booksru.shared.Line;

public class LineSuggestOracle extends SuggestOracle {

  private final class LineSuggestion implements Suggestion {
    private final String displayString;
    private final String replacementText;

    public LineSuggestion(String displayString, String replacementText) {
      this.displayString = displayString;
      this.replacementText = replacementText;
    }

    public String getDisplayString() {
      return displayString;
    }

    public String getReplacementString() {
      return replacementText;
    }

  }

  private Line[] lines;

  public LineSuggestOracle() {
  }

  private String emphasize(String text, String query) {
    if (text == null || query == null) {
      return text;
    }
    int pos = text.toLowerCase().indexOf(query.toLowerCase());
    if (pos != -1) {
      text = text.substring(0, pos) + "<b style='color: blue !important;'>"
          + text.substring(pos, pos + query.length()) + "</b>"
          + text.substring(pos + query.length());
    }
    return text;
  }

  @Override
  public boolean isDisplayStringHTML() {
    return true;
  }

  @Override
  public void requestDefaultSuggestions(Request request, Callback callback) {
    requestSuggestions(request, callback);
  }

  @Override
  public void requestSuggestions(final Request request, final Callback callback) {
    ArrayList<Suggestion> suggestions = new ArrayList<Suggestion>();
    String query = request.getQuery();
    if (query != null) {
      suggestions.add(new LineSuggestion(emphasize(query, query), query));
    }
    if (lines != null) {
      int min = Integer.MAX_VALUE;
      int max = 0;
      for (int i = 0; i < lines.length; i++) {
        int reads = lines[i].getReads();
        if (reads > max) {
          max = reads;
        }
        if (reads < min) {
          min = reads;
        }
      }

      for (int i = 0; i < lines.length; i++) {
        int color = Math.round((lines[i].getReads() - min) / (max - min + 1) * 255);
        String replacementText = lines[i].getLineText();
        String emph = emphasize(replacementText, query);
        emph = "<span style='color: rgb(" + color + ", " + color + ", " + color + ");'>(" + color
            + ")" + emph + "</span>";
        suggestions.add(new LineSuggestion(emph + " (" + lines[i] + ")", replacementText));
      }
    }
    Response response = new Response(suggestions);
    callback.onSuggestionsReady(request, response);
  }

  public void setSuggestions(Line[] lines) {
    this.lines = lines;
  }

}
