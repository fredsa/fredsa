package sqlmapreduce.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class Sqlmapreduce implements EntryPoint {

  private final RpcServiceAsync service = GWT.create(RpcService.class);

  public void onModuleLoad() {
    DatastorePage datastorePage = new DatastorePage(service);
    SqlPage sqlPage = new SqlPage(service);

    RootLayoutPanel root = RootLayoutPanel.get();
    root.add(datastorePage);
    root.add(sqlPage);

    root.setWidgetLeftWidth(datastorePage, 0, Unit.PX, 50, Unit.PCT);
    root.setWidgetRightWidth(sqlPage, 0, Unit.PX, 50, Unit.PCT);
  }
}
