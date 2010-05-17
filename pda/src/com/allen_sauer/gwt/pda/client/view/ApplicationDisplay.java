package com.allen_sauer.gwt.pda.client.view;

import com.allen_sauer.gwt.pda.client.presenter.ApplicationPresenter.Display;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class ApplicationDisplay extends Composite implements Display {

  interface ApplicationDisplayUiBinder extends UiBinder<Widget, ApplicationDisplay> {
  }

  private static ApplicationDisplayUiBinder uiBinder = GWT.create(ApplicationDisplayUiBinder.class);

  @UiField
  Panel editArea;

  @UiField
  Panel resultArea;

  @UiField
  Panel searchBarArea;

  @UiField
  Element spinner;

  public ApplicationDisplay() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public Panel getEditArea() {
    return editArea;
  }

  public Panel getResultArea() {
    return resultArea;
  }

  public Panel getSearchBarArea() {
    return searchBarArea;
  }

  public Element getSpinner() {
    return spinner;
  }

}
