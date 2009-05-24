package fredsa.booksru.client.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class PageView extends FlowPanel //implements HasValueAddHandlers<String> 
{

  public PageView() {
    setStylePrimaryName("pageview");
  }

  public void addLine(String text) {
    add(new Label(text));
  }

  public void removeLine() {
    remove(getWidgetCount() - 1);
  }

  //  public HandlerRegistration addValueAddHandler(ValueAddHandler<String> handler) {
  //    return addHandler(handler, ValueAddEvent.getType());
  //  }
  //
  //  public Page getPage() {
  //    return page;
  //  }
  //
  //  public void onLineAdded(Line line) {
  //    if (lineWidget != null) {
  //      lineWidget.setEditable(false);
  //    }
  //    lineWidget.addValueChangeHandler(new ValueChangeHandler<String>() {
  //
  //      public void onValueChange(ValueChangeEvent<String> event) {
  //        ValueAddEvent.fire(PageView.this, event.getValue());
  //      }
  //    });
  //    add(lineWidget);
  //  }
  //
  //  public void setCurrentLineEditable(boolean editable) {
  //    if (lineWidget != null) {
  //      lineWidget.setEditable(editable);
  //    }
  //  }
  //
  //  public void setPage(Page page) {
  //    this.page = page;
  //    page.setPageEventListener(this);
  //  }

}