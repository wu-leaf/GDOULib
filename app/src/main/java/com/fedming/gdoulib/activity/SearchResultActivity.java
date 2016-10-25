package com.fedming.gdoulib.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fedming.gdoulib.R;
import com.fedming.gdoulib.adapter.BooksItemAdapter;
import com.fedming.gdoulib.bean.BooksItem;
import com.fedming.gdoulib.bean.CommonException;
import com.fedming.gdoulib.biz.BooksItemBiz;
import com.fedming.gdoulib.util.Constaint;
import com.fedming.gdoulib.util.RecyclerItemClickListener;
import com.umeng.analytics.MobclickAgent;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * 查找结果返回页
 */
public class SearchResultActivity extends AppCompatActivity {

    private String query_str;
    private String query_str_encode;

    private String query_url;

    private TextView footer_text;

    private int mCurrentPage = 1;

    private BooksItemBiz booksItemBiz;
    private BooksItemAdapter booksItemAdapter;
    private List<BooksItem> mBooksItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar progress_bar_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        query_str = intent.getStringExtra("query");
        try {
            query_str_encode = URLEncoder.encode(query_str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        setTitle("关键词：" + query_str);

        booksItemBiz = new BooksItemBiz();

        progress_bar_list = (ProgressBar) findViewById(R.id.progress_bar_list);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        booksItemAdapter = new BooksItemAdapter(this);
        recyclerView.setAdapter(booksItemAdapter);
        getBooksItem(booksItemAdapter, mCurrentPage, false);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    int lastVisibleItem = manager.findLastCompletelyVisibleItemPosition();
                    int totalItem = manager.getItemCount();
                    if (lastVisibleItem == (totalItem - 1)) {
                        mCurrentPage++;
                        getBooksItem(booksItemAdapter, mCurrentPage, false);
                        footer_text = (TextView) findViewById(R.id.footer_text);
                        if (footer_text != null) {
                            footer_text.setVisibility(View.VISIBLE);
                        }
                    }
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener
                (this, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        try{
                            BooksItem item = booksItemAdapter.getmBooksList().get(position);
                            Intent startActivityIntent = new Intent(SearchResultActivity.this, BookDetailActivity.class);
                            startActivityIntent.putExtra("url", item.getBookLink());
                            startActivity(startActivityIntent);
                        }catch (Exception e){
//                            e.printStackTrace();
                        }


                    }
                })
        );

    }

    public void getBooksItem(BooksItemAdapter adapter, int current_page, boolean forced) {
        LoadBooksItemTask loadBooksItemTask = new LoadBooksItemTask(adapter, forced);

        loadBooksItemTask.execute(current_page);

    }


    class LoadBooksItemTask extends AsyncTask<Integer, Integer, List<BooksItem>> {

        private BooksItemAdapter booksItemAdapter;
        private boolean isForced;

        public LoadBooksItemTask(BooksItemAdapter adapter, boolean forced) {
            super();
            booksItemAdapter = adapter;
            isForced = forced;
        }

        @Override
        protected List<BooksItem> doInBackground(Integer... params) {

            query_url = Constaint.HOSTPART1 + query_str_encode + Constaint.HOSTPART2 + mCurrentPage + Constaint.HOSTPART3;

            try {
                return booksItemBiz.getBooksItem(query_url);
            } catch (CommonException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(List<BooksItem> booksItems) {
            if (booksItems.isEmpty()) {
                progress_bar_list.setVisibility(View.GONE);
//                Toast.makeText(SearchResultActivity.this,"此检索无相关记录~", Toast.LENGTH_LONG).show();
                footer_text = (TextView) findViewById(R.id.footer_text);
                assert footer_text != null;
                footer_text.setVisibility(View.VISIBLE);
                footer_text.setText("没有更多相关记录了~");
                return;
            }

            if (isForced) {
                booksItemAdapter.getmBooksList().clear();
            }
            mBooksItems.addAll(booksItems);
            booksItemAdapter.addBooks(booksItems);
            booksItemAdapter.notifyDataSetChanged();
            progress_bar_list.setVisibility(View.GONE);
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
