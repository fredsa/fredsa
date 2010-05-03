package com.allen_sauer.gwt.pda.client;

import com.allen_sauer.gwt.pda.client.ContactViewPresenter.Display;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class ContactViewDisplay extends Composite implements Display {

  interface ContactDisplayUiBinder extends UiBinder<Widget, ContactViewDisplay> {
  }

  private static ContactDisplayUiBinder uiBinder = GWT.create(ContactDisplayUiBinder.class);

  @UiField
  Label displayName;

  @UiField
  CheckBox enabledCheckbox;

  @UiField
  Label firstName;

  @UiField
  Label lastName;

  @UiField
  FocusPanel panel;

  public ContactViewDisplay(Contact contact) {
    initWidget(uiBinder.createAndBindUi(this));
    firstName.setText(contact.getFirstName());
    lastName.setText(contact.getLastName());
    displayName.setText(contact.getDisplayName());
    enabledCheckbox.setValue(contact.isEnabled());
  }

  public String getDisplayName() {
    return displayName.getText();
  }

  public HasClickHandlers getEditButton() {
    return panel;
  }

}
