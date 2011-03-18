package listen2spell.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import listen2spell.shared.Word;

public interface WordServiceAsync {
  void getWords(String input, AsyncCallback<Word[]> callback) throws IllegalArgumentException;
}
