package fixhtml5.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import fixhtml5.shared.Case;

public class CaseWidget extends Composite {

  interface CaseWidgetUiBinder extends UiBinder<Widget, CaseWidget> {
  }

  private static CaseWidgetUiBinder uiBinder = GWT.create(CaseWidgetUiBinder.class);

  @UiField
  Button button;

  @UiField
  Element caseTitle;

  @UiField
  TextArea htmlTextArea;

  @UiField
  TextArea scriptTextArea;

  @UiField
  HTML result;

  public CaseWidget(Case c) {
    initWidget(uiBinder.createAndBindUi(this));
    caseTitle.setInnerHTML(c.getKey() + " - " + c.getName());
    htmlTextArea.setValue(c.getHtml());
    scriptTextArea.setValue(c.getScript());
    result.setHTML(c.getHtml());
    eval(c.getScript());

    button.addClickHandler(new ClickHandler() {

      @Override
      public void onClick(ClickEvent event) {
        // TODO Auto-generated method stub

      }
    });
  }

  private native void eval(String script)/*-{
    $wnd.eval(script);
  }-*/;

}
