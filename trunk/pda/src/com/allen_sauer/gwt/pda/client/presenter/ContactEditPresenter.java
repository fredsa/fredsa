package com.allen_sauer.gwt.pda.client.presenter;

import com.allen_sauer.gwt.pda.client.PdaServiceAsync;


public class ContactEditPresenter {

  public static interface Display {
  }

  private Display display;

  private PdaServiceAsync pdaService;

  public ContactEditPresenter(PdaServiceAsync pdaService) {
    this.pdaService = pdaService;
  }

  public void bindDisplay(final Display display) {
    this.display = display;
  }

}
