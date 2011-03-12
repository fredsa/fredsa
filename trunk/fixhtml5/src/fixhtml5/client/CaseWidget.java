package fixhtml5.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
  Button save;

  @UiField
  Element caseTitle;

  @UiField
  TextArea htmlTextArea;

  @UiField
  TextArea scriptTextArea;

  @UiField
  HTML result;

  private final Case c;

  private CaseServiceAsync caseService;

  public CaseWidget(Case c) {
    this.c = c;
    initWidget(uiBinder.createAndBindUi(this));
    caseTitle.setInnerHTML(c.getKey() + " - " + c.getName());

    htmlTextArea.setValue(c.getHtml());
    htmlTextArea.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        dirty();
      }
    });

    scriptTextArea.setValue(c.getScript());
    scriptTextArea.addChangeHandler(new ChangeHandler() {
      @Override
      public void onChange(ChangeEvent event) {
        dirty();
      }
    });

    save.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        save();
      }
    });

    run();
  }

  private void run() {
    result.setHTML(c.getHtml());
    try {
    eval(c.getScript());
    } catch (Throwable e) {
      result.setHTML("eval threw: " + e);
    }
    clean();
  }

  protected void save() {
    clean();
    result.setHTML("Saving...");
    c.setHtml(htmlTextArea.getValue());
    c.setScript(scriptTextArea.getValue());
    caseService.updateCase(c, new AsyncCallback<Void>() {
      @Override
      public void onSuccess(Void ignore) {
        result.setHTML("Case saved");
        run();
      }

      @Override
      public void onFailure(Throwable caught) {
        dirty();
        result.setHTML("Save failed: " + caught);
      }
    });
  }

  private void clean() {
    //    save.setEnabled(false);
  }

  private void dirty() {
    //    save.setEnabled(true);
  }

  private native void eval(String script)/*-{
		$wnd.eval(script);
  }-*/;

  public void setService(CaseServiceAsync caseService) {
    this.caseService = caseService;
  }

}
