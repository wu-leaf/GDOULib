package com.fedming.gdoulib.biz;

import com.fedming.gdoulib.bean.BooksItem;
import com.fedming.gdoulib.bean.CommonException;
import com.fedming.gdoulib.util.DataUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class BooksItemBiz {

	public List<BooksItem> getBooksItem(String urlStr) throws CommonException {

		String htmlStr = DataUtil.doGet(urlStr);

		List<BooksItem> booksItems = new ArrayList<>();
		BooksItem booksItem = null;

		Document doc = Jsoup.parse(htmlStr, "http://210.38.138.7:8080/");
		Elements ai = doc.select("ul.list");
		Elements units = ai.select("li");

		for (int i = 0; i < units.size(); i++) {
			booksItem = new BooksItem();
			Element unit_ele = units.get(i);
//			int j = i+1;

			String bookinfo = unit_ele.text();
			String info1[] = bookinfo.split("书目信息：");
			String info2[] = info1[1].split("馆藏");
			String info3[] = info2[1].split("索取号");
            String info4[] = info3[0].split("可借");

			Element unit_ele_p = unit_ele.select("p").first();
			Element unit_ele_a = unit_ele_p.select("a").first();
			String bookname = unit_ele_a.text();
			String booklink = unit_ele_a.attr("abs:href");

			booksItem.setBookName("【"+bookname+"】");
			booksItem.setBookLink(booklink);
			booksItem.setBookInfo("馆藏"+info4[0]+"      可借"+info4[1]+"\n"+"概览："+info2[0]);

			booksItems.add(booksItem);

		}

		return booksItems;

	}

}
