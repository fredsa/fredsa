package com.allen_sauer.gwt.pda.client.command;

import com.allen_sauer.gwt.pda.client.shared.Contact;

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
