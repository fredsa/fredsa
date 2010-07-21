package com.allen_sauer.gwt.pda.server;

import com.allen_sauer.gwt.pda.client.PdaService;
import com.allen_sauer.gwt.pda.client.command.Command;
import com.allen_sauer.gwt.pda.client.command.Response;
import com.allen_sauer.gwt.pda.client.command.SearchCommand;
import com.allen_sauer.gwt.pda.client.command.SearchResponse;
import com.allen_sauer.gwt.pda.client.shared.Contact;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class PdaServiceImpl extends RemoteServiceServlet implements PdaService {

  public <R extends Response> R execute(Command<R> command) {
		if (command instanceof SearchCommand) {
			SearchCommand cmd = (SearchCommand) command;
			String firstName = cmd.getText();
			Contact contact = makeFirstName(firstName);
			Contact contact2 = makeFirstName(firstName);

      return (R) new SearchResponse(new Contact[] {contact, contact2});
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
