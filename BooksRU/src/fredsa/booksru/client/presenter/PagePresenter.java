package fredsa.booksru.client.presenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import fredsa.booksru.client.BookService;
import fredsa.booksru.client.BookServiceAsync;
import fredsa.booksru.client.view.LineView;
import fredsa.booksru.client.view.PageView;
import fredsa.booksru.shared.Line;
import fredsa.booksru.shared.Page;

public class PagePresenter {

  private final BookServiceAsync bookService = GWT.create(BookService.class);

  private Line line = Line.NULL_LINE;

  private LineView lineView = new LineView();

  private Page page = new Page();

  private PageView pageView = new PageView();

  private Line[] suggestedLines;

  private HTML waitingMessage;

  public PagePresenter(RootPanel rootPanel) {
    rootPanel.add(new Label(line.getLineText()));
    waitingMessage = new HTML("Loading...");
    rootPanel.add(waitingMessage);
    rootPanel.add(pageView);
    rootPanel.add(lineView);
    requestLines(line);

    lineView.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        pageView.addLine(event.getValue());
        lineView.clear();
        requestLines(line = getOrMakeLine(line, event.getValue()));
      }

    });
  }

  private Line getOrMakeLine(Line previousLine, String lineText) {
    for (int i = 0; i < suggestedLines.length; i++) {
      if (suggestedLines[i].getLineText().equals(lineText)) {
        return suggestedLines[i];
      }
    }
    return new Line(previousLine, lineText);
  }

  private void requestLines(final Line previousLine) {
    bookService.getLineSuggestions(previousLine, new AsyncCallback<Line[]>() {

      public void onFailure(Throwable caught) {
        waitingMessage.setHTML(waitingMessage.getHTML() + ".");
        new Timer() {

          @Override
          public void run() {
            requestLines(previousLine);
          }
        }.schedule(5000);
      }

      public void onSuccess(Line[] suggestedLines) {
        PagePresenter.this.suggestedLines = suggestedLines;
        page.setSuggestions(previousLine, suggestedLines);
        if (previousLine == Line.NULL_LINE) {
          waitingMessage.removeFromParent();
        }
        lineView.setSuggestions(suggestedLines);
      }
    });
  }

}
