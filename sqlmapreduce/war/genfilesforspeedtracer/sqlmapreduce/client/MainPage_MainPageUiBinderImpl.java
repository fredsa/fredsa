package sqlmapreduce.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiBinderUtil;
import com.google.gwt.user.client.ui.Widget;

public class MainPage_MainPageUiBinderImpl implements UiBinder<com.google.gwt.user.client.ui.Widget, sqlmapreduce.client.MainPage>, sqlmapreduce.client.MainPage.MainPageUiBinder {

  public com.google.gwt.user.client.ui.Widget createAndBindUi(final sqlmapreduce.client.MainPage owner) {

    sqlmapreduce.client.MainPage_MainPageUiBinderImpl_GenBundle clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay = (sqlmapreduce.client.MainPage_MainPageUiBinderImpl_GenBundle) GWT.create(sqlmapreduce.client.MainPage_MainPageUiBinderImpl_GenBundle.class);
    sqlmapreduce.client.MainPage_MainPageUiBinderImpl_GenCss_style style = clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay.style();
    java.lang.String domId0 = com.google.gwt.dom.client.Document.get().createUniqueId();
    com.google.gwt.user.client.ui.TextArea sql = (com.google.gwt.user.client.ui.TextArea) GWT.create(com.google.gwt.user.client.ui.TextArea.class);
    java.lang.String domId1 = com.google.gwt.dom.client.Document.get().createUniqueId();
    com.google.gwt.user.client.ui.Button go = (com.google.gwt.user.client.ui.Button) GWT.create(com.google.gwt.user.client.ui.Button.class);
    java.lang.String domId2 = com.google.gwt.dom.client.Document.get().createUniqueId();
    com.google.gwt.user.client.ui.Label results = (com.google.gwt.user.client.ui.Label) GWT.create(com.google.gwt.user.client.ui.Label.class);
    com.google.gwt.user.client.ui.ScrollPanel f_ScrollPanel2 = (com.google.gwt.user.client.ui.ScrollPanel) GWT.create(com.google.gwt.user.client.ui.ScrollPanel.class);
    com.google.gwt.user.client.ui.HTMLPanel f_HTMLPanel1 = new com.google.gwt.user.client.ui.HTMLPanel("<div>SQL query:</div> <span id='" + domId0 + "'></span> <span id='" + domId1 + "'></span> <div>Results:</div> <span id='" + domId2 + "'></span>");

    sql.setText("select * from message");
    sql.setStyleName("" + style.sql() + "");
    go.setHTML("Go!");
    f_ScrollPanel2.add(results);
    f_ScrollPanel2.setStyleName("" + style.sp() + "");

    UiBinderUtil.TempAttachment attachRecord0 = UiBinderUtil.attachToDom(f_HTMLPanel1.getElement());
    com.google.gwt.user.client.Element domId0Element = com.google.gwt.dom.client.Document.get().getElementById(domId0).cast();
    com.google.gwt.user.client.Element domId1Element = com.google.gwt.dom.client.Document.get().getElementById(domId1).cast();
    com.google.gwt.user.client.Element domId2Element = com.google.gwt.dom.client.Document.get().getElementById(domId2).cast();
    attachRecord0.detach();
    f_HTMLPanel1.addAndReplaceElement(sql, domId0Element);
    f_HTMLPanel1.addAndReplaceElement(go, domId1Element);
    f_HTMLPanel1.addAndReplaceElement(f_ScrollPanel2, domId2Element);


    final com.google.gwt.event.dom.client.ClickHandler handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames1 = new com.google.gwt.event.dom.client.ClickHandler() {
      public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
        owner.onClick(event);
      }
    };
    go.addClickHandler(handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames1);

    final com.google.gwt.event.dom.client.KeyDownHandler handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames2 = new com.google.gwt.event.dom.client.KeyDownHandler() {
      public void onKeyDown(com.google.gwt.event.dom.client.KeyDownEvent event) {
        owner.onKeyDown(event);
      }
    };
    sql.addKeyDownHandler(handlerMethodWithNameVeryUnlikelyToCollideWithUserFieldNames2);

    owner.go = go;
    owner.results = results;
    owner.sql = sql;
    clientBundleFieldNameUnlikelyToCollideWithUserSpecifiedFieldOkay.style().ensureInjected();

    return f_HTMLPanel1;
  }
}
