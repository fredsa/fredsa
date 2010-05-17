package com.allen_sauer.gwt.pda.client;

import com.allen_sauer.gwt.pda.client.command.Command;
import com.allen_sauer.gwt.pda.client.command.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface PdaServiceAsync {
	void execute(Command command, AsyncCallback<Response> callback);
}
