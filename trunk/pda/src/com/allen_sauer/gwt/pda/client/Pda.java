package com.allen_sauer.gwt.pda.client;

import com.allen_sauer.gwt.pda.client.presenter.ApplicationPresenter;
import com.allen_sauer.gwt.pda.client.view.ApplicationDisplay;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.RootPanel;

public class Pda implements EntryPoint {
  public void onModuleLoad() {
    // set uncaught exception handler
    GWT.setUncaughtExceptionHandler(new GWT.UncaughtExceptionHandler() {

      public void onUncaughtException(Throwable throwable) {
        String text = "Uncaught exception: ";
        while (throwable != null) {
          StackTraceElement[] stackTraceElements = throwable.getStackTrace();
          text += throwable.toString() + "\n";
          for (StackTraceElement element : stackTraceElements) {
            text += "    at " + element + "\n";
          }
          throwable = throwable.getCause();
          if (throwable != null) {
            text += "Caused by: ";
          }
        }
        DialogBox dialogBox = new DialogBox(true);
        dialogBox.getElement().getStyle().setProperty("backgroundColor", "#ABCDEF");
        System.err.print(text);
        text = text.replaceAll(" ", "&nbsp;");
        dialogBox.setHTML("<pre>" + text + "</pre>");
        dialogBox.center();
      }
    });

    // use a deferred command so that the handler catches onModuleLoad2() exceptions
    DeferredCommand.addCommand(new Command() {

      public void execute() {
        onModuleLoad2();
      }
    });
  }

  public void onModuleLoad2() {
    final PdaServiceAsync pdaService = new FakePdaService(
        (PdaServiceAsync) GWT.create(PdaService.class));
    final HandlerManager eventBus = new HandlerManager(null);

    ApplicationDisplay applicationDisplay = new ApplicationDisplay();
    RootPanel.get().add(applicationDisplay);

    ApplicationPresenter applicationPresenter = new ApplicationPresenter(pdaService, eventBus);
    applicationPresenter.bindDisplay(applicationDisplay);
  }
}
