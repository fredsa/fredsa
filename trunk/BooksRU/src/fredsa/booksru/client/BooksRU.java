package fredsa.booksru.client;

import java.util.Date;
import java.util.List;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import fredsa.booksru.shared.Book;

/*
 * * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BooksRU implements EntryPoint {

  private static native void setWindowFocus() /*-{
    $wnd.focus();
  }-*/;

  private final BookServiceAsync bookService = GWT.create(BookService.class);

  public void onModuleLoad() {
    Log.setUncaughtExceptionHandler();

    DeferredCommand.addCommand(new Command() {
      public void execute() {
        onModuleLoad2();
      }
    });
  }

  private void onModuleLoad2() {
    RootPanel rootPanel = RootPanel.get();

    Event.addNativePreviewHandler(new NativePreviewHandler() {
      public void onPreviewNativeEvent(NativePreviewEvent event) {
        if ((event.getTypeInt() & Event.KEYEVENTS) != 0) {
          NativeEvent evt = event.getNativeEvent();
          if (evt.getKeyCode() == 63240 /*F5*/|| evt.getKeyCode() == 114/*R*/&& evt.getMetaKey()) {
            Window.Location.reload();
            event.cancel();
          }
        }
      }
    });
    setWindowFocus();

    rootPanel.add(new Label(new Date().toString()));

    final BookGrid bookResultsGrid = new BookGrid();
    rootPanel.add(bookResultsGrid);

    bookService.getBooks(new AsyncCallback<List<Book>>() {

      public void onFailure(Throwable caught) {
        Log.warn("Failed to retrieve books", caught);
      }

      public void onSuccess(List<Book> result) {
        bookResultsGrid.setBooks(result);
      }
    });

  }
}
