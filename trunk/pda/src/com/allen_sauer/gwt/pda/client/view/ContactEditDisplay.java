package com.allen_sauer.gwt.pda.client.view;

import com.allen_sauer.gwt.pda.client.presenter.ContactEditPresenter.Display;
import com.allen_sauer.gwt.pda.client.shared.Contact;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ContactEditDisplay extends Composite implements Display {

  interface ContactDisplayUiBinder extends UiBinder<Widget, ContactEditDisplay> {
  }

  private static ContactDisplayUiBinder uiBinder = GWT.create(ContactDisplayUiBinder.class);

  @UiField
  TextBox displayName;

  @UiField
  CheckBox enabledCheckbox;

  @UiField
  TextBox firstName;

  @UiField
  TextBox lastName;

  @UiField
  HTMLPanel panel;

  public ContactEditDisplay(Contact contact) {
    initWidget(uiBinder.createAndBindUi(this));
    firstName.setValue(contact.getFirstName());
    lastName.setValue(contact.getLastName());
    displayName.setValue(contact.getDisplayName());
    enabledCheckbox.setValue(contact.isEnabled());
  }

  public HasClickHandlers getDisplayName() {
    return displayName;
  }

  public HasClickHandlers getEnabledCheckbox() {
    return enabledCheckbox;
  }

  public HasClickHandlers getFirstName() {
    return firstName;
  }

  public HasClickHandlers getLastName() {
    return lastName;
  }

}
