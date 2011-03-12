package fixhtml5.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Navigator;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import fixhtml5.shared.Case;

public class CaseWidget extends Composite {

  interface CaseWidgetUiBinder extends UiBinder<Widget, CaseWidget> {
  }

  private static CaseWidgetUiBinder uiBinder = GWT.create(CaseWidgetUiBinder.class);

  private ChangeHandler changeHandler = new ChangeHandler() {
    @Override
    public void onChange(ChangeEvent event) {
      dirty();
    }
  };

  private ClickHandler clickHandler = new ClickHandler() {
    @Override
    public void onClick(ClickEvent event) {
      save();
    }
  };

  @UiField
  Button save;

  @UiField
  Element caseTitle;

  @UiField
  TextArea htmlTextArea;

  @UiField
  TextArea scriptTextArea;

  @UiField
  HTML testOutput;

  @UiField
  TextBox userAgent;

  @UiField
  TextArea findings;

  private final Case c;

  private CaseServiceAsync caseService;

  private KeyDownHandler keyDownHandler = new KeyDownHandler() {
    @Override
    public void onKeyDown(KeyDownEvent event) {
      if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && event.isAnyModifierKeyDown()) {
        save();
      }
    }
  };

  public CaseWidget(Case c) {
    this.c = c;
    initWidget(uiBinder.createAndBindUi(this));
    caseTitle.setInnerHTML(c.getKey() + " - " + c.getName());

    htmlTextArea.setValue(c.getHtml());
    htmlTextArea.addChangeHandler(changeHandler);
    htmlTextArea.addKeyDownHandler(keyDownHandler);

    scriptTextArea.setValue(c.getScript());
    scriptTextArea.addChangeHandler(changeHandler);
    scriptTextArea.addKeyDownHandler(keyDownHandler);

    save.addClickHandler(clickHandler);
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    execute();
  }

  private void lookupUserAgent() {
    String ua = Navigator.getUserAgent();

    caseService.getPrettyUserAgent(ua, new AsyncCallback<String>() {
      @Override
      public void onSuccess(String result) {
        userAgent.setValue("sadfsdfsd");
      }

      @Override
      public void onFailure(Throwable caught) {
        testOutput.setHTML("Unable to lookup user agent: " + caught);
        Window.alert("2");
      }
    });

  }

  private void execute() {
    testOutput.setHTML(c.getHtml());
    try {
      eval(c.getScript());
    } catch (Throwable e) {
      testOutput.setHTML("eval threw: <pre>" + e + "</pre>");
    }
    clean();
    lookupUserAgent();
  }

  protected void save() {
    clean();
    testOutput.setHTML("Saving...");
    c.setHtml(htmlTextArea.getValue());
    c.setScript(scriptTextArea.getValue());
    caseService.updateCase(c, new AsyncCallback<Void>() {
      @Override
      public void onSuccess(Void ignore) {
        testOutput.setHTML("Case saved");
        execute();
      }

      @Override
      public void onFailure(Throwable caught) {
        dirty();
        testOutput.setHTML("Save failed: " + caught);
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
