package com.allen_sauer.gwt.pda.client;

import java.io.Serializable;

public class Contact implements Serializable {
  private String displayName;

  private boolean enabled = true;
  private String firstName;
  private String id;
  private String lastName;

  public Contact() {
  }

  public String getDisplayName() {
    return displayName;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getId() {
    return id;
  }

  public String getLastName() {
    return lastName;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
}
