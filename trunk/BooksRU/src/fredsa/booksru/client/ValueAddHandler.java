package fredsa.booksru.client;

import com.google.gwt.event.shared.EventHandler;

public interface ValueAddHandler<I> extends EventHandler {

  void onValueAdd(ValueAddEvent<I> event);
}
