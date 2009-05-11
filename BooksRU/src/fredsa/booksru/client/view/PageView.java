package fredsa.booksru.client.view;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import fredsa.booksru.client.HasValueAddHandlers;
import fredsa.booksru.client.LineWidget;
import fredsa.booksru.client.ValueAddEvent;
import fredsa.booksru.client.ValueAddHandler;
import fredsa.booksru.shared.Line;

import java.util.Date;

public class PageView extends FlowPanel implements HasValueAddHandlers<String> {

  private LineWidget lineWidget;

  public PageView() {
    add(new Label(new Date().toString()));
  }

  public void addLine(Line line) {
    lineWidget = new LineWidget(line);
    lineWidget.addValueChangeHandler(new ValueChangeHandler<String>() {

      public void onValueChange(ValueChangeEvent<String> event) {
        ValueAddEvent.fire(PageView.this, event.getValue());
      }
    });
    add(lineWidget);
  }

  public HandlerRegistration addValueAddHandler(ValueAddHandler<String> handler) {
    return addHandler(handler, ValueAddEvent.getType());
  }

  public void setCurrentLineEditable(boolean editable) {
    if (lineWidget != null) {
      lineWidget.setEditable(editable);
    }
  }

}