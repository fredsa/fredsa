package com.allen_sauer.gwt.pda.server;

import com.allen_sauer.gwt.pda.client.Command;
import com.allen_sauer.gwt.pda.client.Contact;
import com.allen_sauer.gwt.pda.client.PdaService;
import com.allen_sauer.gwt.pda.client.Response;
import com.allen_sauer.gwt.pda.client.SearchCommand;
import com.allen_sauer.gwt.pda.client.SearchResponse;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PdaServiceImpl extends RemoteServiceServlet implements PdaService {

	public Response execute(Command command) {
		if (command instanceof SearchCommand) {
			SearchCommand cmd = (SearchCommand) command;
			String firstName = cmd.getText();
			Contact contact = makeFirstName(firstName);
			Contact contact2 = makeFirstName(firstName);
			
			return new SearchResponse(new Contact[] { contact, contact2 });
		}
		return null;
	}

	private Contact makeFirstName(String firstName) {
		Contact contact = new Contact();
		contact.setFirstName(firstName);
		contact.setLastName("bar");
		contact.setDisplayName("Ford Prefect");
		return contact;
	}
}
