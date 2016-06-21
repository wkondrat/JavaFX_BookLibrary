package com.capgemini.javafx.dataprovider.data;

/**
 * @author WKONDRAT
 *
 */
public class BookVO {
	
	private Long id;
	private String title;
	private String authors;
	private BookStatusVO bookStatus;
	
	public BookVO(Long id, String title, String authors, BookStatusVO bookStatus) {
		this.id = id;
		this.title = title;
		this.authors = authors;
		this.bookStatus = bookStatus;
	}
	
	public BookVO(String title, String authors, BookStatusVO bookStatus) {
		this.title = title;
		this.authors = authors;
		this.bookStatus = bookStatus;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getAuthors() {
		return authors;
	}
	
	public void setAuthors(String authors) {
		this.authors = authors;
	}
	
	public BookStatusVO getBookStatus() {
		return bookStatus;
	}
	
	public void setBookStatus(BookStatusVO bookStatus) {
		this.bookStatus = bookStatus;
	}
	
	@Override
	public String toString() {
		return "Book [title=" + title + ", bookStatus=" + bookStatus + ", authors=" + authors + "]";
	}
}
