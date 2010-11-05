package sqlmapreduce.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

public class MainPage extends Composite {

  interface MainPageUiBinder extends UiBinder<Widget, MainPage> {
  }

  private static MainPageUiBinder uiBinder = GWT.create(MainPageUiBinder.class);

  @UiField
  Button go;

  @UiField
  Label results;

  @UiField
  TextArea sql;

  private final SqlServiceAsync service;

  public MainPage(SqlServiceAsync service) {
    initWidget(uiBinder.createAndBindUi(this));
    this.service = service;
  }

  @Override
  protected void onLoad() {
    super.onLoad();
    sql.setFocus(true);
  }

  @UiHandler("go")
  void onClick(ClickEvent e) {
    execute();
  }

  @UiHandler("sql")
  void onKeyDown(KeyDownEvent e) {
    if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER && e.isAnyModifierKeyDown()) {
      e.preventDefault();
      execute();
    }
  }

  private void execute() {
    go.setEnabled(false);
    results.setText("");
    service.greetServer(sql.getText(), new AsyncCallback<String>() {

      public void onFailure(Throwable caught) {
        results.setStylePrimaryName("error");
        if (caught instanceof RuntimeException) {
          results.setText(caught.getMessage());
        } else {
          results.setText(caught.toString());
        }
        go.setEnabled(true);
      }

      public void onSuccess(String result) {
        results.setStylePrimaryName("results");
        results.setText(result);
        sql.setFocus(true);
        sql.selectAll();
        go.setEnabled(true);
      }
    });
  }

}
