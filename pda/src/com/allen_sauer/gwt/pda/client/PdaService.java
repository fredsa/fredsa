package com.allen_sauer.gwt.pda.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc")
public interface PdaService extends RemoteService {
	Response execute(Command command);
}
