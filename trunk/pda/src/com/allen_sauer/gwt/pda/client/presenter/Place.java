package com.allen_sauer.gwt.pda.client.presenter;

import com.google.gwt.user.client.History;

public class Place {

  public enum Command {
    EDIT, SEARCH,
  }

  private static char DELIMITER = ',';

  public static void newItem(Command command, String value) {
    History.newItem(command.name() + DELIMITER + value);
  }

  private Command command;

  private String value;

  public Place(Command command, String value) {
    this.command = command;
    this.value = value;
  }

  public Place(String token) {
    int comma = token.indexOf(DELIMITER);
    if (comma == -1) {
      return;
    }
    command = Command.valueOf(token.substring(0, comma));
    value = token.substring(comma + 1);
  }

  public Command getCommand() {
    return command;
  }

  public String getToken() {
    return command.name() + DELIMITER + value;
  }

  public String getValue() {
    return value;
  }
}
