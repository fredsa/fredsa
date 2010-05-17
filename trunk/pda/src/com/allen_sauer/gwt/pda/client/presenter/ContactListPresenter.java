package com.allen_sauer.gwt.pda.client.presenter;

import com.allen_sauer.gwt.pda.client.PdaServiceAsync;
import com.allen_sauer.gwt.pda.client.event.SearchEvent;
import com.allen_sauer.gwt.pda.client.event.SearchEventHandler;
import com.allen_sauer.gwt.pda.client.event.SearchResultEvent;
import com.allen_sauer.gwt.pda.client.event.SearchResultEventHandler;
import com.allen_sauer.gwt.pda.client.shared.Contact;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.UIObject;

public class ContactListPresenter {

  public static interface Display {
    UIObject getHeader();

    void setContactList(Contact[] contacts);
  }

  private Display display;

  private final HandlerManager eventBus;

  private PdaServiceAsync pdaService;

  public ContactListPresenter(PdaServiceAsync pdaService, HandlerManager eventBus) {
    this.pdaService = pdaService;
    this.eventBus = eventBus;

    eventBus.addHandler(SearchEvent.TYPE, new SearchEventHandler() {
      public void handle(SearchEvent event) {
        display.getHeader().setVisible(false);
        display.setContactList(new Contact[] {});
      }
    });

    eventBus.addHandler(SearchResultEvent.TYPE,
        new SearchResultEventHandler() {
      public void handle(SearchResultEvent event) {
        display.getHeader().setVisible(true);
        display.setContactList(event.getContacts());
      }
    });

  }

  public void bindDisplay(Display display) {
    this.display = display;
    display.getHeader().setVisible(false);
  }
}
