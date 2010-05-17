package com.allen_sauer.gwt.pda.client.presenter;

import com.allen_sauer.gwt.pda.client.PdaServiceAsync;
import com.allen_sauer.gwt.pda.client.event.SearchEvent;
import com.allen_sauer.gwt.pda.client.event.SearchEventHandler;
import com.allen_sauer.gwt.pda.client.event.SearchResultEvent;
import com.allen_sauer.gwt.pda.client.event.SearchResultEventHandler;
import com.allen_sauer.gwt.pda.client.view.ContactListDisplay;
import com.allen_sauer.gwt.pda.client.view.SearchDisplay;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Panel;

public class ApplicationPresenter {
  public interface Display {
    Panel getEditArea();

    Panel getResultArea();

    Panel getSearchBarArea();

    Element getSpinner();
  }

  private Display display;

  private final HandlerManager eventBus;

  private PdaServiceAsync pdaService;

  public ApplicationPresenter(final PdaServiceAsync pdaService, final HandlerManager eventBus) {
    this.pdaService = pdaService;
    this.eventBus = eventBus;

    eventBus.addHandler(SearchEvent.TYPE, new SearchEventHandler() {

      public void handle(SearchEvent event) {
        display.getSpinner().getStyle().setVisibility(Visibility.VISIBLE);
      }
    });

    eventBus.addHandler(SearchResultEvent.TYPE, new SearchResultEventHandler() {

      public void handle(SearchResultEvent event) {
        display.getSpinner().getStyle().setVisibility(Visibility.HIDDEN);
      }
    });
  }

  public void bindDisplay(final ApplicationPresenter.Display display) {
    this.display = display;

    SearchDisplay searchDisplay = new SearchDisplay();

    ContactListDisplay contactListDisplay = new ContactListDisplay("Your results:", eventBus,
        pdaService);
    display.getSearchBarArea().add(searchDisplay);
    display.getResultArea().add(contactListDisplay);

    SearchPresenter searchPresenter = new SearchPresenter(pdaService, eventBus);
    searchPresenter.bindDisplay(searchDisplay);
    searchPresenter.start();

    ContactListPresenter contactListPresenter = new ContactListPresenter(pdaService, eventBus);
    contactListPresenter.bindDisplay(contactListDisplay);

    ContactEditPresenter contactEditPresenter = new ContactEditPresenter(pdaService);
    //    contactEditPresenter.bindDisplay(contactEditDisplay);
  }
}
