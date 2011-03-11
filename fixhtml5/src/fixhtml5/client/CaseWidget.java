package fixhtml5.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.UIObject;

import fixhtml5.shared.Case;

public class CaseWidget extends UIObject {

  private static CaseUiBinder uiBinder = GWT.create(CaseUiBinder.class);

  interface CaseUiBinder extends UiBinder<Element, CaseWidget> {
  }

  @UiField
  Element caseTitle;

  public CaseWidget(Case c) {
    setElement(uiBinder.createAndBindUi(this));
    caseTitle.setInnerHTML(c.getName());
  }

}
