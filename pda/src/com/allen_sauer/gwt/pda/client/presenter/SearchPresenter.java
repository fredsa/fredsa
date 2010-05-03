package com.allen_sauer.gwt.pda.client.presenter;

import com.allen_sauer.gwt.pda.client.PdaServiceAsync;
import com.allen_sauer.gwt.pda.client.event.SearchEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TextBox;

public class SearchPresenter extends Composite {
  public interface Display {
    Focusable getFocusable();

    TextBox getSearchBox();

    HasClickHandlers getSearchButton();
  }

  private Display display;

  private final HandlerManager eventBus;

  private PdaServiceAsync pdaService;

  public SearchPresenter(PdaServiceAsync pdaService, HandlerManager eventBus) {
    this.pdaService = pdaService;
    this.eventBus = eventBus;
  }

  public void bindDisplay(final SearchPresenter.Display display) {
    this.display = display;
    display.getSearchBox().addKeyDownHandler(new KeyDownHandler() {
      public void onKeyDown(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
          doSearch(display.getSearchBox().getText());
        }
      }
    });
    display.getSearchButton().addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        doSearch(display.getSearchBox().getText());
      }
    });
  }

  public void start() {
    display.getFocusable().setFocus(true);
  }

  private void doSearch(String text) {
    eventBus.fireEvent(new SearchEvent(text));
    display.getSearchBox().selectAll();
  }
}
