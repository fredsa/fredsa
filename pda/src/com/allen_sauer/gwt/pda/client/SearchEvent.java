package com.allen_sauer.gwt.pda.client;

import com.google.gwt.event.shared.GwtEvent;

public class SearchEvent extends GwtEvent<SearchEventHandler> {
	public static final Type<SearchEventHandler> TYPE = new Type<SearchEventHandler>();
	private final String query;

	public SearchEvent(String query) {
		this.query = query;
	}
	@Override
	protected void dispatch(SearchEventHandler handler) {
		handler.handle(this);
	}

	@Override
	public Type<SearchEventHandler> getAssociatedType() {
		return TYPE;
	}
	public String getQuery() {
		return query;
	}

}
