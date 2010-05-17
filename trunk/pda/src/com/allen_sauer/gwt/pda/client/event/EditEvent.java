package com.allen_sauer.gwt.pda.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class EditEvent extends GwtEvent<EditEventHandler> {
  public static final Type<EditEventHandler> TYPE = new Type<EditEventHandler>();
  private final String contactId;

  public EditEvent(String contactId) {
    this.contactId = contactId;
  }

  @Override
  public com.google.gwt.event.shared.GwtEvent.Type<EditEventHandler> getAssociatedType() {
    return TYPE;
  }

  public String getContactId() {
    return contactId;
  }

  @Override
  protected void dispatch(EditEventHandler handler) {
    handler.handle(this);
  }

}
