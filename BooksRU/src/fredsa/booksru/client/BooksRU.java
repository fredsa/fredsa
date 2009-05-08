package fredsa.booksru.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.RootPanel;

import com.allen_sauer.gwt.log.client.Log;

import fredsa.booksru.client.presenter.PagePresenter;
import fredsa.booksru.client.view.PageView;

/*
 * * Entry point classes define <code>onModuleLoad()</code>.
 */
public class BooksRU implements EntryPoint {

  private static native void setWindowFocus() /*-{
    $wnd.focus();
  }-*/;

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

    PageView pageView = new PageView();
    rootPanel.add(pageView);
    PagePresenter pagePresenter = new PagePresenter(pageView);
  }
}
