package com.allen_sauer.gwt.pda.client.command;



@SuppressWarnings("serial")
public class SearchCommand implements Command<SearchResponse> {

	
	private String text;

	// For GTW RPC only
	private SearchCommand() {
	}

	public SearchCommand(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
