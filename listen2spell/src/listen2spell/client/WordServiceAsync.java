package listen2spell.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import listen2spell.shared.Word;

public interface WordServiceAsync {
  void getSpokenWord(String name, AsyncCallback<Word> callback);

  void getWordList(AsyncCallback<String[]> callback);
}
