package com.allen_sauer.gwt.pda.client;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;

public class ContactListEditor {

	private PdaServiceAsync pdaService;

	private Display display;

	private final HandlerManager eventBus;

	static interface Display {
		UIObject getHeader();

		void setContactList(Contact[] contacts);
	}

	public ContactListEditor(PdaServiceAsync pdaService, HandlerManager eventBus) {
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
