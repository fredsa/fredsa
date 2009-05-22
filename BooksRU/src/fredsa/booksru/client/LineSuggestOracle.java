package fredsa.booksru.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.SuggestOracle;

import fredsa.booksru.shared.Line;

import java.util.ArrayList;

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

  @Override
  public boolean isDisplayStringHTML() {
    return true;
  }

  @Override
  public void requestSuggestions(final Request request, final Callback callback) {
    ArrayList<Suggestion> suggestions = new ArrayList<Suggestion>();
    String query = request.getQuery();
    suggestions.add(new LineSuggestion(query, query));
    for (int i = 0; i < lines.length; i++) {
      String replacementText = lines[i].getLineText();
      String displayText = replacementText;
      String substringMatch = displayText.substring(0, query.length());
      if (substringMatch.equalsIgnoreCase(query)) {
        displayText = "<b style='color: blue !important;'>" + substringMatch + "</b>"
            + displayText.substring(query.length());
      }
      suggestions.add(new LineSuggestion(displayText, replacementText));
    }
    Response response = new Response(suggestions);
    callback.onSuggestionsReady(request, response);
  }

  public void setSuggestions(Line[] lines) {
    this.lines = lines;
  }
}
