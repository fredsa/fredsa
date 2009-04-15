package fredsa.booksru.client;

import com.google.gwt.event.dom.client.HasAllMouseHandlers;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.ui.Widget;

public class MouseHandlerDecorator {

  private static final String CSS_SUFFIX_MOUSE_HOVER = "HOVER";

  public MouseHandlerDecorator(final Widget widget) {
    HasAllMouseHandlers h = (HasAllMouseHandlers) widget;

    h.addMouseOutHandler(new MouseOutHandler() {
      public void onMouseOut(MouseOutEvent event) {
        widget.removeStyleDependentName(CSS_SUFFIX_MOUSE_HOVER);
      }
    });

    h.addMouseOverHandler(new MouseOverHandler() {
      public void onMouseOver(MouseOverEvent event) {
        widget.addStyleDependentName(CSS_SUFFIX_MOUSE_HOVER);
      }
    });

  }
}
