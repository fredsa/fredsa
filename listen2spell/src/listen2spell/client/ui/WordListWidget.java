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
    for (int i = 0; i < words.length; i++) {
      Label dashes = new Label(words[i]);
      grid.setWidget(i, 0, dashes);
    }
  }
}
