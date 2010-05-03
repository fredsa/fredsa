package com.allen_sauer.gwt.pda.client.view;

import com.allen_sauer.gwt.pda.client.presenter.SearchPresenter.Display;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class SearchDisplay extends Composite implements Display {

	private static SearchDisplayUiBinder uiBinder = GWT
			.create(SearchDisplayUiBinder.class);

	interface SearchDisplayUiBinder extends UiBinder<Widget, SearchDisplay> {
	}

	public SearchDisplay() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	@UiField
	TextBox searchBox;

	@UiField
	Button searchButton;

	public Focusable getFocusable() {
		return searchBox;
	}

	public HasClickHandlers getSearchButton() {
		return searchButton;
	}

	public TextBox getSearchBox() {
		return searchBox;
	}

}
