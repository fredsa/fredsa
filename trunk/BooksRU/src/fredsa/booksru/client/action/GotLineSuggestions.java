package fredsa.booksru.client.action;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.rpc.AsyncCallback;

import fredsa.booksru.shared.Line;

public abstract class GotLineSuggestions implements AsyncCallback<GetLineSuggestionsReponse> {

  abstract void got(Line[] suggestions);

  public void onFailure(Throwable caught) {
    Log.fatal(this.getClass().getName(), caught);
  }

  public void onSuccess(GetLineSuggestionsReponse result) {
    got(result.getSuggestions());
  }
}
