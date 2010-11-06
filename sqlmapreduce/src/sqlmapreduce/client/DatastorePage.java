package sqlmapreduce.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

import sqlmapreduce.shared.Constants;

public class DatastorePage extends Composite {

  interface DatastorePageUiBinder extends UiBinder<Widget, DatastorePage> {
  }

  private static DatastorePageUiBinder uiBinder = GWT.create(DatastorePageUiBinder.class);

  @UiField
  Button go;

  @UiField
  Button initDatastore;

  @UiField
  HTML results;

  @UiField
  TextArea sql;

  private final RpcServiceAsync service;

  public DatastorePage(RpcServiceAsync service) {
    this.service = service;
    initWidget(uiBinder.createAndBindUi(this));
    sql.setText(Constants.INITIAL_SQL);
  }

  @UiHandler("go")
  void onGoClick(ClickEvent e) {
    execute();
  }

  @UiHandler("initDatastore")
  void onInitDatastoreClick(ClickEvent e) {
    initDatastore.setEnabled(false);
    service.initDatastore(new AsyncCallback<String>() {

      public void onFailure(Throwable caught) {
        initDatastore.setEnabled(true);
        Window.alert("Initialization failed: " + caught.getMessage());
      }

      public void onSuccess(String result) {
        initDatastore.setEnabled(true);
        results.setHTML(result);
      }
    });
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
    service.executeDatastoreQuery(sql.getText(), new AsyncCallback<String>() {

      public void onFailure(Throwable caught) {
        results.setStylePrimaryName("error");
        if (caught instanceof RuntimeException) {
          results.setHTML(caught.getMessage());
        } else {
          results.setHTML(caught.toString());
        }
        go.setEnabled(true);
      }

      public void onSuccess(String result) {
        results.setStylePrimaryName("results");
        results.setHTML(result);
        sql.setFocus(true);
        sql.selectAll();
        go.setEnabled(true);
      }
    });
  }

}
