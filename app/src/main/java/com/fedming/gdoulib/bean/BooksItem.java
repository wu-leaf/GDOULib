package com.fedming.gdoulib.bean;

/**
 *  搜索结果展示的图书Item
 */
public class BooksItem {

	private int id;
	private String book_link;
	private String book_name;
	private String book_info;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBookLink() {
		return book_link;
	}

	public void setBookLink(String book_link) {
		this.book_link = book_link;
	}

	public String getBookName() {
		return book_name;
	}

	public void setBookName(String book_name) {
		this.book_name = book_name;
	}

	public String getBookInfo() {
		return book_info;
	}

	public void setBookInfo(String book_info) {
		this.book_info = book_info;
	}

	@Override
	public String toString() {
		return "[BookName:" + book_name + "BookLink:" + book_link
				+ "BookInfo:" + book_info + "]";
	}

}
