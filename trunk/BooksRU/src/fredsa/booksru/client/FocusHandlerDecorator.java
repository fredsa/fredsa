package fredsa.booksru.client;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.HasAllFocusHandlers;
import com.google.gwt.user.client.ui.Widget;

public class FocusHandlerDecorator {

  private static final String CSS_SUFFIX_FOCUS = "FOCUS";

  public FocusHandlerDecorator(final Widget widget) {
    HasAllFocusHandlers h = (HasAllFocusHandlers) widget;

    h.addFocusHandler(new FocusHandler() {
      public void onFocus(FocusEvent event) {
        widget.addStyleDependentName(CSS_SUFFIX_FOCUS);
      }
    });

    h.addBlurHandler(new BlurHandler() {
      public void onBlur(BlurEvent event) {
        widget.removeStyleDependentName(CSS_SUFFIX_FOCUS);
      }
    });

  }
}
