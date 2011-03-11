package fixhtml5.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;

import fixhtml5.shared.Case;

public class Main implements EntryPoint {

  private final CaseServiceAsync caseService = GWT.create(CaseService.class);

  @Override
  public void onModuleLoad() {
    // set uncaught exception handler
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {
      @Override
      public void onUncaughtException(Throwable throwable) {
        String text = "Uncaught exception: ";
        while (throwable != null) {
          StackTraceElement[] stackTraceElements = throwable.getStackTrace();
          text += throwable.toString() + "\n";
          for (StackTraceElement stackTraceElement : stackTraceElements) {
            text += "    at " + stackTraceElement + "\n";
          }
          throwable = throwable.getCause();
          if (throwable != null) {
            text += "Caused by: ";
          }
        }
        DialogBox dialogBox = new DialogBox(true, false);
        DOM.setStyleAttribute(dialogBox.getElement(), "backgroundColor", "#ABCDEF");
        System.err.print(text);
        text = text.replaceAll(" ", "&nbsp;");
        dialogBox.setHTML("<pre>" + text + "</pre>");
        dialogBox.center();
      }
    });

    // use a deferred command so that the handler catches onModuleLoad2() exceptions
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        onModuleLoad2();
      }
    });
  }

  private void onModuleLoad2() {
    final Element wait = Document.get().getElementById("wait");
    wait.setInnerHTML("Fetching cases...");
    caseService.getCase(0, new AsyncCallback<Case>() {
      @Override
      public void onSuccess(Case result) {
        wait.removeFromParent();
        RootPanel.getBodyElement().appendChild(new CaseWidget(result).getElement());
      }

      @Override
      public void onFailure(Throwable caught) {
        wait.setInnerHTML("FAILED TO LOAD CASES: " + caught);
      }
    });
  }
}
