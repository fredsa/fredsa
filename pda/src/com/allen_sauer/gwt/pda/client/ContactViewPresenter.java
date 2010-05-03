package com.allen_sauer.gwt.pda.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

public class ContactViewPresenter {

  static interface Display {
    String getDisplayName();
    HasClickHandlers getEditButton();
  }
  EventBus eventBus;

  private ClickHandler clickHandler = new ClickHandler() {
    public void onClick(ClickEvent event) {
      editContact(display);
    }
  };

  private Contact contact = new Contact();
  private Display display;

  private PdaServiceAsync pdaService;

  public ContactViewPresenter(PdaServiceAsync pdaService) {
    this.pdaService = pdaService;
  }

  public void bindDisplay(final Display display) {
    this.display = display;
    display.getEditButton().addClickHandler(clickHandler);
  }

  private void editContact(Display display) {
    eventBus.fireEvent(new EditEvent(contact.getId()));
  }
}
