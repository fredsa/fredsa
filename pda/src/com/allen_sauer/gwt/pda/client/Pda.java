package com.allen_sauer.gwt.pda.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class Pda implements EntryPoint {

	public void onModuleLoad() {
		final PdaServiceAsync pdaService = GWT.create(PdaService.class);
		// final EventBus eventBus = new EventBus();
		final HandlerManager eventBus = new HandlerManager(null);

		SearchDisplay searchDisplay = new SearchDisplay();

		ContactListDisplay contactListDisplay = new ContactListDisplay(
				"Your results:", pdaService);
		RootPanel.get().add(searchDisplay);
		RootPanel.get().add(contactListDisplay);

		SearchEditor searchEditor = new SearchEditor(pdaService, eventBus);
		searchEditor.bindDisplay(searchDisplay);
		searchEditor.start();

		eventBus.addHandler(SearchEvent.TYPE, new SearchEventHandler() {

			public void handle(SearchEvent event) {
				pdaService.execute(new SearchCommand(event.getQuery()),
						new AsyncCallback<Response>() {
							public void onSuccess(Response response) {
								if (response instanceof SearchResponse) {
									SearchResponse res = (SearchResponse) response;
									eventBus.fireEvent(new SearchResultEvent(
											res.getContacts()));
								}
							}

							public void onFailure(Throwable e) {
								e.printStackTrace();
								Window.alert(e.toString());
							}
						});

			}
		});

		ContactListEditor contactListEditor = new ContactListEditor(pdaService,
				eventBus);
		contactListEditor.bindDisplay(contactListDisplay);
	}
}
