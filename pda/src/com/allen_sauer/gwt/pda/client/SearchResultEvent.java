package com.allen_sauer.gwt.pda.client;

import com.google.gwt.event.shared.GwtEvent;

public class SearchResultEvent extends GwtEvent<SearchResultEventHandler> {
	public static final Type<SearchResultEventHandler> TYPE = new Type<SearchResultEventHandler>();
	private final Contact[] contacts;

	public SearchResultEvent(Contact[] contacts) {
		this.contacts = contacts;
	}
	@Override
	protected void dispatch(SearchResultEventHandler handler) {
		handler.handle(this);
	}

	@Override
	public Type<SearchResultEventHandler> getAssociatedType() {
		return TYPE;
	}
	public Contact[] getContacts() {
		return contacts;
	}

}
