package com.allen_sauer.gwt.pda.client.view;

import com.allen_sauer.gwt.pda.client.PdaServiceAsync;
import com.allen_sauer.gwt.pda.client.presenter.ContactListPresenter;
import com.allen_sauer.gwt.pda.client.presenter.ContactViewPresenter;
import com.allen_sauer.gwt.pda.client.presenter.ContactListPresenter.Display;
import com.allen_sauer.gwt.pda.client.shared.Contact;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class ContactListDisplay extends Composite implements Display {

  interface ContactListDisplayUiBinder extends
  UiBinder<Widget, ContactListDisplay> {
  }

  private static ContactListDisplayUiBinder uiBinder = GWT
  .create(ContactListDisplayUiBinder.class);

  @UiField
  VerticalPanel contactListPanel;

  @UiField
  Label header;

  private PdaServiceAsync pdaService;

  public ContactListDisplay(String title, PdaServiceAsync pdaService) {
    this.pdaService = pdaService;
    initWidget(uiBinder.createAndBindUi(this));
    header.setText(title);
  }

  public UIObject getHeader() {
    return header;
  }

  public void setContactList(Contact[] contacts) {
    contactListPanel.clear();
    contactListPanel.add(new HTML("<hr>"));
    for (Contact contact : contacts) {
      ContactViewDisplay display = new ContactViewDisplay(contact);
      ContactViewPresenter presenter = new ContactViewPresenter(pdaService);
      contactListPanel.add(display);
      contactListPanel.add(new HTML("<hr>"));
      presenter.bindDisplay(display);
    }
  }
}
