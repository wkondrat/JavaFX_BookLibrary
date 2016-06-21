package com.capgemini.javafx.dataprovider;

import java.util.Collection;

import com.capgemini.javafx.dataprovider.data.BookStatusVO;
import com.capgemini.javafx.dataprovider.data.BookVO;
import com.capgemini.javafx.dataprovider.impl.DataProviderImpl;


public interface DataProvider {

	/**
	 * Instance of this interface.
	 */
	DataProvider INSTANCE = new DataProviderImpl();

	/**
	 * Finds books with their title containing specified string and/or given
	 * bookStatus.
	 *
	 * @param title
	 *            string contained in title
	 * @param bookStatus
	 *            bookStatus
	 * @return collection of persons matching the given criteria
	 */
	public Collection<BookVO> findBooksByParameters(String title, String authors, BookStatusVO bookStatus) throws Exception;
}
