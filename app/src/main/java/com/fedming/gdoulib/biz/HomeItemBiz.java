package com.fedming.gdoulib.biz;

import com.fedming.gdoulib.bean.CommonException;
import com.fedming.gdoulib.bean.HomeItem;
import com.fedming.gdoulib.util.DataUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;


public class HomeItemBiz {

    public List<HomeItem> getHomeItems(String urlStr) throws CommonException {

        String htmlStr = DataUtil.doGet(urlStr);
        List<HomeItem> homeItems = new ArrayList<>();
        HomeItem homeItem = null;

        Document doc = Jsoup.parse(htmlStr, "http://210.38.138.7:8080/");
        Elements ai = doc.select("div.list").first().select("div.con");

        Element hotlend = ai.get(2);

        if (!hotlend.select("a[href]").isEmpty()) {
            Element hot0 = hotlend.select("a[href]").get(0);
            String bookname0 = hot0.text();
            String booklink0 = hot0.attr("abs:href");
            homeItem = new HomeItem();
            homeItem.setBookName(bookname0);
            homeItem.setBookLink(booklink0);
            homeItems.add(homeItem);
            Element hot1 = hotlend.select("a[href]").get(1);
            String bookname1 = hot1.text();
            String booklink1 = hot1.attr("abs:href");
            homeItem = new HomeItem();
            homeItem.setBookName(bookname1);
            homeItem.setBookLink(booklink1);
            homeItems.add(homeItem);
        } else {
            homeItem = new HomeItem();
            homeItem.setBookName("暂无数据");
            homeItem.setBookLink(null);
            homeItems.add(homeItem);
            homeItem = new HomeItem();
            homeItem.setBookName("暂无数据");
            homeItem.setBookLink(null);
            homeItems.add(homeItem);
        }

        Element theysee = ai.last();
        if (!theysee.select("a[href]").isEmpty()) {
            Elements so = theysee.select("a[href]");
            for (int i = 0; i < so.size(); i++) {
                homeItem = new HomeItem();
                Element element = so.get(i);
                String bookname = element.text();
                String booklink = element.attr("abs:href");
                homeItem.setBookName(bookname);
                homeItem.setBookLink(booklink);

                homeItems.add(homeItem);
            }
        } else {
            for (int i = 0; i < 10; i++) {
                homeItem = new HomeItem();
                homeItem.setBookName("暂无数据");
                homeItem.setBookLink(null);
                homeItems.add(homeItem);
            }
        }
        return homeItems;

    }
}
