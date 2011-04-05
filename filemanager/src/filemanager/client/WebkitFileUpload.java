package filemanager.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FileUpload;

public class WebkitFileUpload extends FileUpload {

  public final static class File extends JavaScriptObject {
    protected File() {
    }

    native String getFileName() /*-{
      return this.fileName;
    }-*/;

    native int getFileSize() /*-{
      return this.fileSize;
    }-*/;

    native String getlastModifiedDate() /*-{
      this.lastModifiedDate;
    }-*/;

    native String getName() /*-{
      return this.name;
    }-*/;

    native int getSize() /*-{
      return this.size;
    }-*/;

    native int getType() /*-{
      return this.type;
    }-*/;

    native String getWebkitRelativePath() /*-{
      return this.webkitRelativePath;
    }-*/;
  }

  public WebkitFileUpload() {
    setDirectory(true);
  }

  JsArray<File> getFiles() {
    return getFiles(getElement());
  }

  void setDirectory(boolean directory) {
    getElement().setAttribute("webkitdirectory", directory ? "" : null);
  }

  void setMultiple(boolean multiple) {
    getElement().setAttribute("multiple", multiple ? "" : null);
  }

  private native JsArray<File> getFiles(Element e) /*-{
    return e.files;
  }-*/;

}
