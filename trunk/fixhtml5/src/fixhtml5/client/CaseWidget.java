package fixhtml5.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.PreElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import fixhtml5.shared.Case;

public class CaseWidget extends Composite {

  private static CaseWidgetUiBinder uiBinder = GWT.create(CaseWidgetUiBinder.class);

  interface CaseWidgetUiBinder extends UiBinder<Widget, CaseWidget> {
  }

  @UiField
  Element caseTitle;

  @UiField
  Button button;

  @UiField
  PreElement pre;

  public CaseWidget(Case c) {
    initWidget(uiBinder.createAndBindUi(this));
    caseTitle.setInnerHTML(c.getName());
    button.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        // TODO Auto-generated method stub

      }
    });
  }

}
