package com.allen_sauer.gwt.pda.client;

import com.allen_sauer.gwt.pda.client.command.Command;
import com.allen_sauer.gwt.pda.client.command.Response;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rpc")
public interface PdaService extends RemoteService {
  <R extends Response> R execute(Command<R> command);
}
