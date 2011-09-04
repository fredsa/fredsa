package listen2spell.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import listen2spell.shared.Util;

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
    grid.getColumnFormatter().setStylePrimaryName(0, "numColumn");
    grid.getColumnFormatter().setStylePrimaryName(1, "wordColumn");
    grid.getColumnFormatter().setStylePrimaryName(2, "numColumn");
    grid.getColumnFormatter().setStylePrimaryName(3, "wordColumn");
    for (int i = 0; i < words.length; i++) {
      Label number = new Label(i + 1 + ":");
      grid.setWidget(i / 2, i % 2 * 2, number);

      Label word = new Label(Util.rightPadDashes("", words[i].length()));
      grid.setWidget(i / 2, i % 2 * 2 + 1, word);
    }
  }
}
