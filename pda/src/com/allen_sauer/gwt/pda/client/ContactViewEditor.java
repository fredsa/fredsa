package com.allen_sauer.gwt.pda.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.Window;

public class ContactViewEditor {

  static interface Display {
    HasClickHandlers getPanel();
  }

  private ClickHandler clickHandler = new ClickHandler() {
    public void onClick(ClickEvent event) {
      editContact(display);
    }
  };
  private Display display;

  private PdaServiceAsync pdaService;

  public ContactViewEditor(PdaServiceAsync pdaService) {
    this.pdaService = pdaService;
  }

  public void bindDisplay(final Display display) {
    this.display = display;
    display.getPanel().addClickHandler(clickHandler);
  }

  private void editContact(Display display) {
    Window.alert(getClass().getName() + " editing");
  }
}
