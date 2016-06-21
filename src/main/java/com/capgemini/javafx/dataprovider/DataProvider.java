package com.capgemini.javafx.dataprovider;

import java.util.Collection;

import com.capgemini.javafx.dataprovider.data.BookStatusVO;
import com.capgemini.javafx.dataprovider.data.BookVO;
import com.capgemini.javafx.dataprovider.impl.DataProviderImpl;


public interface DataProvider {

	DataProvider INSTANCE = new DataProviderImpl();

	public Collection<BookVO> findBooksByParameters(String title, String authors, BookStatusVO bookStatus) throws Exception;
	public void addBook(String title, String authors, BookStatusVO bookStatus) throws Exception;
}
