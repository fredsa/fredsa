package fredsa.booksru.client.action;

import fredsa.booksru.shared.Line;

public class GetLineSuggestionsReponse implements Response {
  private final Line[] lineSuggestions;

  public GetLineSuggestionsReponse(Line[] lineSuggestions) {
    this.lineSuggestions = lineSuggestions;
  }

  public Line[] getSuggestions() {
    return lineSuggestions;
  }
}
