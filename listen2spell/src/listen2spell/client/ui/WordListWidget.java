package listen2spell.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WordListWidget extends Composite {

  interface WordListUiBinder extends UiBinder<Widget, WordListWidget> {
  }

  private static WordListUiBinder uiBinder = GWT.create(WordListUiBinder.class);

  @UiField
  FlexTable grid;

  private final String[] words;

  public WordListWidget(String[] words) {
    this.words = words;
    initWidget(uiBinder.createAndBindUi(this));
    grid.setBorderWidth(1);
    grid.getColumnFormatter().setWidth(0, "300");
    grid.getColumnFormatter().setWidth(1, "300");
    for (int i = 0; i < words.length; i++) {
      Label dashes = new Label(i + 1 + ": " + words[i]);
      grid.setWidget(i / 2, i % 2, dashes);
    }
  }
}
