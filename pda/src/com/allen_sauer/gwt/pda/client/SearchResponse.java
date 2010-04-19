package com.allen_sauer.gwt.pda.client;

@SuppressWarnings("serial")
public class SearchResponse implements Response {

	private Contact[] contacts;

	// For GWT RPC only
	private SearchResponse() {
	}

	public SearchResponse(Contact[] contacts) {
		this.contacts = contacts;
	}

	public Contact[] getContacts() {
		return contacts;
	}

}
