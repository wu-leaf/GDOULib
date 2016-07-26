package com.fedming.gdoulib;

import com.fedming.gdoulib.bean.BooksItem;
import com.fedming.gdoulib.bean.HomeItem;
import com.fedming.gdoulib.biz.BooksItemBiz;
import com.fedming.gdoulib.biz.HomeItemBiz;

import java.util.List;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @org.junit.Test
    public void test() {

        HomeItemBiz biz = new HomeItemBiz();
        String url =
                "http://210.38.138.7:8080/sms/opac/search/showSearch.action?xc=6";
        List<HomeItem> homeItems = null;
        try {
            homeItems =
                    biz.getHomeItems(url);

            for (HomeItem homeItem : homeItems) {
                System.out.println(homeItem);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        BooksItemBiz biz1 = new BooksItemBiz();
        String url1 = "http://210.38.138.7:8080/search?kw=%E9%A3%9F%E5%93%81&xc=6&searchtype=anywords";
        List<BooksItem> booksItems = null;
        try {
            booksItems = biz1.getBooksItem(url1);

            for (BooksItem booksItem : booksItems) {
                System.out.println(booksItem);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }
}