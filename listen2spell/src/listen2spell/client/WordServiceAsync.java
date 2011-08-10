package listen2spell.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import listen2spell.shared.Spoken;

public interface WordServiceAsync {
  void getSpokenWord(String name, AsyncCallback<Spoken> callback);

  void getWordList(AsyncCallback<String[]> callback);
}
