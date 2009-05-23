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
    return pos == -1 ? text : text.substring(0, pos) + "<b style='color: blue !important;'>"
        + text.substring(pos, pos + query.length()) + "</b>" + text.substring(pos + query.length());
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
      for (int i = 0; i < lines.length; i++) {
        String replacementText = lines[i].getLineText();
        suggestions.add(new LineSuggestion(emphasize(replacementText, query) + " ("
            + lines[i].getCompoundKey() + ")", replacementText));
      }
    }
    Response response = new Response(suggestions);
    callback.onSuggestionsReady(request, response);
  }

  public void setSuggestions(Line[] lines) {
    this.lines = lines;
  }

}
