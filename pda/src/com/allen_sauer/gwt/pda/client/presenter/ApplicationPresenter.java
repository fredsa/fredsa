package com.allen_sauer.gwt.pda.client.presenter;

import com.allen_sauer.gwt.pda.client.PdaServiceAsync;
import com.allen_sauer.gwt.pda.client.event.SearchEvent;
import com.allen_sauer.gwt.pda.client.event.SearchEventHandler;
import com.allen_sauer.gwt.pda.client.event.SearchResultEvent;
import com.allen_sauer.gwt.pda.client.event.SearchResultEventHandler;
import com.allen_sauer.gwt.pda.client.presenter.Place.Command;
import com.allen_sauer.gwt.pda.client.view.ContactListDisplay;
import com.allen_sauer.gwt.pda.client.view.SearchDisplay;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Panel;

import java.util.Date;

public class ApplicationPresenter {
  public interface Display {
    Panel getEditArea();

    Panel getResultArea();

    Panel getSearchBarArea();

    Element getSpinner();
  }

  SearchDisplay searchDisplay = new SearchDisplay();

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

    History.addValueChangeHandler(new ValueChangeHandler<String>() {
      public void onValueChange(ValueChangeEvent<String> event) {
        String token = event.getValue();
        //        Window.alert("onValueChange: " + token);
        if (token == null || token.length() == 0) {
          return;
        }
        Place place = new Place(token);
        //        Window.alert("onValueChange: " + place.getValue());

        if (place.getCommand() == Command.EDIT) {
          //          Window.alert("EDIT " + place.getValue());
        } else if (place.getCommand() == Command.SEARCH) {
          //          Window.alert("Search for " + place.getValue());
          SearchPresenter searchPresenter = new SearchPresenter(pdaService, eventBus);
          searchPresenter.bindDisplay(searchDisplay);
          searchPresenter.start(place.getValue());
        } else {
          //          Window.alert("Unhandled: " + token);
          History.newItem(null);
        }
      }
    });
  }

  public void bindDisplay(final ApplicationPresenter.Display display) {
    this.display = display;

    ContactListDisplay contactListDisplay = new ContactListDisplay("Your results:", eventBus,
        pdaService);
    display.getSearchBarArea().add(searchDisplay);
    display.getResultArea().add(contactListDisplay);

    ContactListPresenter contactListPresenter = new ContactListPresenter(pdaService, eventBus);
    contactListPresenter.bindDisplay(contactListDisplay);

    ContactEditPresenter contactEditPresenter = new ContactEditPresenter(pdaService);
    //    contactEditPresenter.bindDisplay(contactEditDisplay);

    Place.newItem(Command.SEARCH, "" + new Date());
  }
}
