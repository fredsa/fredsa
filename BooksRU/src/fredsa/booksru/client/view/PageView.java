package fredsa.booksru.client.view;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import fredsa.booksru.client.HasValueAddHandlers;
import fredsa.booksru.client.LineWidget;
import fredsa.booksru.client.ValueAddHandler;
import fredsa.booksru.shared.Line;

import java.util.Date;

public class PageView extends FlowPanel implements HasValueAddHandlers<String> {

  public PageView() {
    add(new Label(new Date().toString()));
  }

  public void addLine(Line line) {
    LineWidget lineWidget = new LineWidget(line);
    lineWidget.addValueChangeHandler(new ValueChangeHandler<String>() {

      public void onValueChange(ValueChangeEvent<String> event) {
        Window.alert("yes");

      }
    });
    add(lineWidget);
  }

  public HandlerRegistration addValueAddHandler(ValueAddHandler<String> handler) {
    return addHandler(handler, ValueAddEvent.getType());
  }

}