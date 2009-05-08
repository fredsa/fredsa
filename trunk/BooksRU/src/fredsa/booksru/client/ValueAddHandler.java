package fredsa.booksru.client;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.EventHandler;

public interface ValueAddHandler<I> extends EventHandler {

  void onValueChange(ValueChangeEvent<I> event);
}
