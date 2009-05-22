package fredsa.booksru.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

import fredsa.booksru.client.BookService;
import fredsa.booksru.client.BookServiceAsync;
import fredsa.booksru.client.LineSuggestionWidget;
import fredsa.booksru.client.view.PageView;
import fredsa.booksru.shared.Line;
import fredsa.booksru.shared.Page;

public class PagePresenter {

  private final BookServiceAsync bookService = GWT.create(BookService.class);

  private LineSuggestionWidget lineSuggestionWidget = new LineSuggestionWidget();

  private Page page = new Page();
  private PageView pageView = new PageView();

  private HTML waitingMessage;

  public PagePresenter(RootPanel rootPanel) {
    waitingMessage = new HTML("Loading...");
    rootPanel.add(waitingMessage);
    rootPanel.add(pageView);
    rootPanel.add(lineSuggestionWidget);
    requestLines(Line.NULL_LINE);

    lineSuggestionWidget.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        pageView.addLine(event.getValue());
        lineSuggestionWidget.setText("");
      }
    });
  }

  private void requestLines(final Line previousLine) {
    bookService.getLineSuggestions(previousLine, new AsyncCallback<Line[]>() {

      public void onFailure(Throwable caught) {
        waitingMessage.setHTML(waitingMessage.getHTML() + ".");
        requestLines(Line.NULL_LINE);
      }

      public void onSuccess(Line[] suggestedLines) {
        page.setSuggestions(previousLine, suggestedLines);
        if (previousLine == Line.NULL_LINE) {
          waitingMessage.removeFromParent();
        }
        lineSuggestionWidget.setSuggestions(suggestedLines);
      }
    });
  }

}
