package com.allen_sauer.gwt.pda.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface PdaServiceAsync {
	void execute(Command command, AsyncCallback<Response> callback);
}
