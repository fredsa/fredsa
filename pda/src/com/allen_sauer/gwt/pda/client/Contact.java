package com.allen_sauer.gwt.pda.client;

import java.io.Serializable;

public class Contact implements Serializable {
	public Contact() {
	}

	private String firstName;
	private String lastName;
	private boolean enabled = true;
	private String displayName;

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getFirstName() {
		return firstName;
	}
}
