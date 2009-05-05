package fredsa.booksru.client;

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

import fredsa.booksru.shared.Line;

public class LineSuggestOracle extends SuggestOracle {

  private final class LineSuggestion implements Suggestion {
    private final Line line;

    public LineSuggestion(Line line) {
      this.line = line;
    }

    public String getDisplayString() {
      return line.getLineText();
    }

    public String getReplacementString() {
      return line.getLineText();
    }

  }

  private static final BookServiceAsync bookService = GWT.create(BookService.class);

  private final Line previousLine;

  public LineSuggestOracle(Line previousLine) {
    this.previousLine = previousLine;
  }

  @Override
  public void requestSuggestions(final Request request, final Callback callback) {
    //Line[] lines = 
    bookService.getLineSuggestions(previousLine, new AsyncCallback<Line[]>() {

      public void onFailure(Throwable caught) {
        Log.debug("Sorry.");
      }

      public void onSuccess(Line[] lines) {
        ArrayList<Suggestion> suggestions = new ArrayList<Suggestion>();
        for (int i = 0; i < lines.length; i++) {
          LineSuggestion suggestion = new LineSuggestion(lines[i]);
          suggestions.add(suggestion);
        }
        Response response = new Response(suggestions);
        callback.onSuggestionsReady(request, response);
      }
    });
  }
}
