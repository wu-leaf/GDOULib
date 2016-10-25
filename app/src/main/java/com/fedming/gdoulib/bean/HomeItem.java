package com.fedming.gdoulib.bean;

/**
 * 装载在主页面的图书item业务逻辑
 */

public class HomeItem {

	private int id;
	private String book_link;
	private String book_name;

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

	@Override
	public String toString() {
		return "[BookName: " + book_name + " BookLink:" + book_link + " ]";
	}

}
