package fredsa.booksru.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
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

  private static final BookServiceAsync bookService = GWT.create(BookService.class);

  private Line[] lines;

  public LineSuggestOracle() {
  }

  private String emphasize(String text, String query) {
    int pos = text.toLowerCase().indexOf(query.toLowerCase());
    return pos == -1 ? text : text.substring(0, pos) + "<b style='color: blue !important;'>"
        + text.substring(pos, pos + query.length()) + "</b>" + text.substring(pos + query.length());
  }

  @Override
  public boolean isDisplayStringHTML() {
    return true;
  }

  @Override
  public void requestSuggestions(final Request request, final Callback callback) {
    ArrayList<Suggestion> suggestions = new ArrayList<Suggestion>();
    String query = request.getQuery();
    suggestions.add(new LineSuggestion(emphasize(query, query), query));
    for (int i = 0; i < lines.length; i++) {
      String replacementText = lines[i].getLineText();
      suggestions.add(new LineSuggestion(emphasize(replacementText, query), replacementText));
    }
    Response response = new Response(suggestions);
    callback.onSuggestionsReady(request, response);
  }

  public void setSuggestions(Line[] lines) {
    this.lines = lines;
  }

}
