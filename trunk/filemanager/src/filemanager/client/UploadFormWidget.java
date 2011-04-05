package filemanager.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import filemanager.client.WebkitFileUpload.File;

public class UploadFormWidget extends Composite {

  interface UploadFormWidgetUiBinder extends UiBinder<Widget, UploadFormWidget> {
  }

  private static UploadFormWidgetUiBinder uiBinder = GWT.create(UploadFormWidgetUiBinder.class);

  @UiField
  WebkitFileUpload fileUpload;

  @UiField
  Button selectButton;

  public UploadFormWidget() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @UiHandler("fileUpload")
  void onChangeFileUpload(ChangeEvent evt) {
    JsArray<File> files = fileUpload.getFiles();
    for (int i = 0; i < files.length(); i++) {
      log(files.get(i).getFileName());
    }
  }

  @UiHandler("selectButton")
  void onClickSelectButton(ClickEvent evt) {
    InputElement fileUploadElement = fileUpload.getElement().cast();
    fileUploadElement.click();
  }


  private native void log(JavaScriptObject o) /*-{
    console.log(o);
  }-*/;

  private native void log(String msg) /*-{
    console.log(msg);
  }-*/;

}
