package com.allen_sauer.gwt.pda.client;

import com.allen_sauer.gwt.pda.client.presenter.ApplicationPresenter;
import com.allen_sauer.gwt.pda.client.view.ApplicationDisplay;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

public class Pda implements EntryPoint {

  public void onModuleLoad() {
    final PdaServiceAsync pdaService = new FakePdaService(
        (PdaServiceAsync) GWT.create(PdaService.class));
    final HandlerManager eventBus = new HandlerManager(null);

    ApplicationDisplay applicationDisplay = new ApplicationDisplay();
    RootPanel.get().add(applicationDisplay);

    ApplicationPresenter applicationPresenter = new ApplicationPresenter(pdaService, eventBus);
    applicationPresenter.bindDisplay(applicationDisplay);
  }
}
