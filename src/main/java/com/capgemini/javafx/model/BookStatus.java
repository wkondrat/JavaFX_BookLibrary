package com.capgemini.javafx.model;

import com.capgemini.javafx.dataprovider.data.BookStatusVO;

public enum BookStatus {

	ANY, MISSING, LOAN, FREE;
	
	public static BookStatus fromBookStatusVO(BookStatusVO bookStatus) {
		return BookStatus.valueOf(bookStatus.name());
	}

	public BookStatusVO toBookStatusVO() {
		if (this == ANY) {
			return null;
		}
		return BookStatusVO.valueOf(this.name());
	}
}
