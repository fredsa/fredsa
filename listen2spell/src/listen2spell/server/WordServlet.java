package listen2spell.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import listen2spell.client.WordService;
import listen2spell.shared.Word;

@SuppressWarnings("serial")
public class WordServlet extends RemoteServiceServlet implements WordService {

  public Word[] getWords(String word) throws IllegalArgumentException {
    Word w = Speech.getWord(word);
    return new Word[] {w};
  }
}
