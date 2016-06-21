package com.capgemini.javafx.model;

import java.util.ArrayList;
import java.util.List;

import com.capgemini.javafx.dataprovider.data.BookVO;

import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;

public class BooksLibrary {
	private final StringProperty title = new SimpleStringProperty();
	private final StringProperty authors = new SimpleStringProperty();
	private final ObjectProperty<BookStatus> bookStatus = new SimpleObjectProperty<>();
	private final ListProperty<BookVO> result = new SimpleListProperty<>(
			FXCollections.observableList(new ArrayList<>()));

	public final String getTitle() {
		return title.get();
	}

	public final void setTitle(String value) {
		title.set(value);
	}

	public StringProperty titleProperty() {
		return title;
	}

	public final BookStatus getBookStatus() {
		return bookStatus.get();
	}

	public final void setBookStatus(BookStatus value) {
		bookStatus.set(value);
	}

	public ObjectProperty<BookStatus> bookStatusProperty() {
		return bookStatus;
	}

	public final List<BookVO> getResult() {
		return result.get();
	}

	public final void setResult(List<BookVO> value) {
		result.setAll(value);
	}

	public ListProperty<BookVO> resultProperty() {
		return result;
	}

	public final String getAuthors() {
		return authors.get();
	}

	public final void setAuthors(String value) {
		authors.set(value);
	}

	public StringProperty authorsProperty() {
		return authors;
	}

	
//	@Override
//	public String toString() {
//		return "BookSearch [title=" + title + ", bookStatus=" + bookStatus + ", result=" + result + "]";
//	}
	@Override
	public String toString() {
		return "BookSearch [title=" + title + ", bookStatus=" + bookStatus + ", authors=" + authors + ", result=" + result + "]";
	}
}
