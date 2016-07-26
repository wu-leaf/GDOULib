package com.fedming.gdoulib.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fedming.gdoulib.R;
import com.umeng.analytics.MobclickAgent;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class BookDetailActivity extends AppCompatActivity {

    private Document doc = null;

    private WebView webView;

    private String title;

    private ProgressBar progress_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LoadBookInfoTask loadBookInfoTask = new LoadBookInfoTask();
        loadBookInfoTask.execute();

        webView = (WebView) findViewById(R.id.book_detail_web);
        progress_bar = (ProgressBar) findViewById(R.id.progress_bar);
    }

    class LoadBookInfoTask extends AsyncTask<Integer, Integer, String> {

        @Override
        protected String doInBackground(Integer... params) {

            Intent intent = getIntent();

            String stringUrl = intent.getStringExtra("url");


            try {
                doc = Jsoup
                        .connect(stringUrl)
                        .get();
                title = doc.title();
                Elements select = doc.select("div.header");
                select.remove();
                Elements select1 = doc.select("div.footer");
                select1.remove();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return doc.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                Toast.makeText(BookDetailActivity.this, "数据异常，请稍后再试~", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
            webView.loadData(doc.toString(), "text/html; charset=UTF-8", null);
            webView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
            setTitle(title);
            progress_bar.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
