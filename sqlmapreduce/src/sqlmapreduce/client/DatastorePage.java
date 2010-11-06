package sqlmapreduce.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
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

  private final SqlServiceAsync service;

  public DatastorePage(SqlServiceAsync service) {
    this.service = service;
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiHandler("initDatastore")
  void onInitDatastoreClick(ClickEvent e) {
    initDatastore.setEnabled(false);
    service.initDatabase(new AsyncCallback<String>() {

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

}
