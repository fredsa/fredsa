package fredsa.booksru.client.view;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import fredsa.booksru.shared.Page;

import java.util.Date;

public class PageView extends FlowPanel //implements HasValueAddHandlers<String> 
{

  private Page page;

  public PageView() {
    add(new Label(new Date().toString()));
  }

  public void addLine(String text) {
    add(new Label(text));
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