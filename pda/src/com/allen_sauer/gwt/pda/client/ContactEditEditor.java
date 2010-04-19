package com.allen_sauer.gwt.pda.client;


public class ContactEditEditor {

  static interface Display {
  }

  private Display display;

  private PdaServiceAsync pdaService;

  public ContactEditEditor(PdaServiceAsync pdaService) {
    this.pdaService = pdaService;
  }

  public void bindDisplay(final Display display) {
    this.display = display;
  }

}
