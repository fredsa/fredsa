package listen2spell.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import listen2spell.shared.Word;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface WordService extends RemoteService {
  Word[] getWords(String name) throws IllegalArgumentException;
}
