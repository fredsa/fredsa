package fredsa.booksru.client;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

public interface HasValueAddHandlers<I> extends HasHandlers {
  HandlerRegistration addValueAddHandler(ValueAddHandler<I> handler);
}
