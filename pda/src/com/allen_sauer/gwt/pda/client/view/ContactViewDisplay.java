package com.allen_sauer.gwt.pda.client.view;

import com.allen_sauer.gwt.pda.client.presenter.ContactViewPresenter.Display;
import com.allen_sauer.gwt.pda.client.shared.Contact;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContactViewDisplay extends Composite implements Display {

  interface ContactDisplayUiBinder extends UiBinder<Widget, ContactViewDisplay> {
  }

  private static ContactDisplayUiBinder uiBinder = GWT.create(ContactDisplayUiBinder.class);

  @UiField
  LabelElement displayName;

  @UiField
  InputElement enabledCheckbox;

  @UiField
  LabelElement firstName;

  @UiField
  LabelElement lastName;

  @UiField
  FocusPanel panel;

  public ContactViewDisplay(Contact contact) {
    initWidget(uiBinder.createAndBindUi(this));
    firstName.setInnerText(contact.getFirstName());
    lastName.setInnerText(contact.getLastName());
    displayName.setInnerText(contact.getDisplayName());
    enabledCheckbox.setChecked(contact.isEnabled());
  }

  public HasClickHandlers getEditButton() {
    return panel;
  }

}
