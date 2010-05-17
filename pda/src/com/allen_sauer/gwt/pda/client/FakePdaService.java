package com.allen_sauer.gwt.pda.client;

import com.allen_sauer.gwt.pda.client.command.Command;
import com.allen_sauer.gwt.pda.client.command.Response;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FakePdaService implements PdaServiceAsync {

  private final PdaServiceAsync realService;

  public FakePdaService(PdaServiceAsync realService) {
    this.realService = realService;
  }

  public void execute(final Command command, final AsyncCallback<Response> callback) {
    new Timer() {

      @Override
      public void run() {
        realService.execute(command, callback);
      }
    }.schedule(400);
  }

}
