package sqlmapreduce.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.RootPanel;

public class Sqlmapreduce implements EntryPoint {

  private final RpcServiceAsync service = GWT.create(RpcService.class);

  public void onModuleLoad() {
    Grid grid = new Grid(1, 2);
    grid.setWidth("100%");
    grid.setWidget(0, 0, new DatastorePage(service));
    grid.setWidget(0, 1, new SqlPage(service));
    RootPanel.get().add(grid);
  }
}
