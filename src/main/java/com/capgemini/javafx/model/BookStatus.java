package com.capgemini.javafx.model;

import com.capgemini.javafx.dataprovider.data.BookStatusVO;


/**
 * @author WKONDRAT
 *
 */
public enum BookStatus {

	ANY, MISSING, LOAN, FREE;
	
	/**
	 * Converts {@link BookStatusVO} to corresponding {@link BookStatus}.
	 *
	 * @param bookStatus
	 *            {@link BookStatusVO} value
	 * @return {@link BookStatus} value
	 */
	public static BookStatus fromBookStatusVO(BookStatusVO bookStatus) {
		return BookStatus.valueOf(bookStatus.name());
	}

	/**
	 * Converts this {@link BookStatus} to corresponding {@link BookStatusVO}. For values that
	 * do not have corresponding value {@code null} is returned.
	 *
	 * @return {@link BookStatusVO} value or {@code null}
	 */
	public BookStatusVO toBookStatusVO() {
		if (this == ANY) {
			return null;
		}
		return BookStatusVO.valueOf(this.name());
	}
}
