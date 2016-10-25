package com.fedming.gdoulib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.fedming.gdoulib.R;
import com.fedming.gdoulib.bean.CommonException;
import com.fedming.gdoulib.bean.HomeItem;
import com.fedming.gdoulib.biz.HomeItemBiz;
import com.fedming.gdoulib.util.Constaint;
import com.fedming.gdoulib.util.NetworkAvailable;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.util.List;

import static com.umeng.update.UpdateConfig.setDeltaUpdate;

/**
 * 主界面：提供搜索框，聊天搜索热点，若干条“他们都在搜”
 */

public class MainActivity extends AppCompatActivity {

    private SearchView searchView;

    private HomeItemBiz homeItemBiz;

    private ProgressBar progressBar, progressBar1;

    //热门借阅TextView
    private TextView tv_hot_lend_0, tv_hot_lend_1;
    //他们正在搜TextView
    private TextView
            tv_their_search_0, tv_their_search_1, tv_their_search_2,
            tv_their_search_3, tv_their_search_4, tv_their_search_5,
            tv_their_search_6, tv_their_search_7, tv_their_search_8, tv_their_search_9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //友盟自动更新
        UmengUpdateAgent.update(this);
        setDeltaUpdate(false);

        initView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    searchView.setFocusable(true);
                    searchView.setFocusableInTouchMode(true);
                    searchView.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });
        }

        homeItemBiz = new HomeItemBiz();
        LoadIndexData loadIndexData = new LoadIndexData();
        loadIndexData.execute();

    }

    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = (SearchView) findViewById(R.id.search_view);

        //点击空白处关闭软键盘
        //noinspection ConstantConditions
        findViewById(R.id.index_content_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_main);
        progressBar1 = (ProgressBar) findViewById(R.id.progress_bar_main1);
        if (searchView != null) {
            searchView.setFocusable(false);
            searchView.setSubmitButtonEnabled(true);
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if (NetworkAvailable.isOnline(MainActivity.this)) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    intent.putExtra("query", query);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "请连接网络后再试", Toast.LENGTH_LONG).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        tv_hot_lend_0 = (TextView) findViewById(R.id.hot_lend_0);
        tv_hot_lend_1 = (TextView) findViewById(R.id.hot_lend_1);

        tv_their_search_0 = (TextView) findViewById(R.id.their_search_0);
        tv_their_search_1 = (TextView) findViewById(R.id.their_search_1);
        tv_their_search_2 = (TextView) findViewById(R.id.their_search_2);
        tv_their_search_3 = (TextView) findViewById(R.id.their_search_3);
        tv_their_search_4 = (TextView) findViewById(R.id.their_search_4);
        tv_their_search_5 = (TextView) findViewById(R.id.their_search_5);
        tv_their_search_6 = (TextView) findViewById(R.id.their_search_6);
        tv_their_search_7 = (TextView) findViewById(R.id.their_search_7);
        tv_their_search_8 = (TextView) findViewById(R.id.their_search_8);
        tv_their_search_9 = (TextView) findViewById(R.id.their_search_9);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class LoadIndexData extends AsyncTask<Integer, Integer, List<HomeItem>> {

        @Override
        protected List<HomeItem> doInBackground(Integer... params) {
            try {
                return homeItemBiz.getHomeItems(Constaint.HOMEITEMURL);
            } catch (CommonException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(final List<HomeItem> homeItems) {
            if (homeItems == null) {

                tv_hot_lend_0.setText("数据异常");
                tv_their_search_0.setText("请稍后再试~");
                progressBar.setVisibility(View.GONE);
                progressBar1.setVisibility(View.GONE);
                return;
            }
            tv_hot_lend_0.setText(homeItems.get(0).getBookName());
            tv_hot_lend_0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
                    if (homeItems.get(0).getBookLink() != null) {
                        intent.putExtra("url", homeItems.get(0).getBookLink());
                        startActivity(intent);
                    }
                }
            });
            tv_hot_lend_1.setText(homeItems.get(1).getBookName());
            tv_hot_lend_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, BookDetailActivity.class);
                    if (homeItems.get(1).getBookLink() != null) {
                        intent.putExtra("url", homeItems.get(1).getBookLink());
                        startActivity(intent);
                    }
                }
            });

            tv_their_search_0.setText(homeItems.get(2).getBookName());
            tv_their_search_0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    if (homeItems.get(2).getBookLink() != null) {
                        String keyword[] = homeItems.get(2).getBookLink().split("kw=");
                        String query[] = keyword[1].split("&searchtype");

                        intent.putExtra("query", query[0]);
                        startActivity(intent);
                    }

                }
            });
            tv_their_search_1.setText(homeItems.get(3).getBookName());
            tv_their_search_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    if (homeItems.get(3).getBookLink() != null) {
                        String keyword[] = homeItems.get(3).getBookLink().split("kw=");
                        String query[] = keyword[1].split("&searchtype");
                        intent.putExtra("query", query[0]);
                        startActivity(intent);
                    }


                }
            });
            tv_their_search_2.setText(homeItems.get(4).getBookName());
            tv_their_search_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    if (homeItems.get(4).getBookLink() != null) {
                        String keyword[] = homeItems.get(4).getBookLink().split("kw=");
                        String query[] = keyword[1].split("&searchtype");
                        intent.putExtra("query", query[0]);
                        startActivity(intent);
                    }

                }
            });
            tv_their_search_3.setText(homeItems.get(5).getBookName());
            tv_their_search_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    if (homeItems.get(5).getBookLink() != null) {
                        String keyword[] = homeItems.get(5).getBookLink().split("kw=");
                        String query[] = keyword[1].split("&searchtype");
                        intent.putExtra("query", query[0]);
                        startActivity(intent);
                    }

                }
            });
            tv_their_search_4.setText(homeItems.get(6).getBookName());
            tv_their_search_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    if (homeItems.get(6).getBookLink() != null) {
                        String keyword[] = homeItems.get(6).getBookLink().split("kw=");
                        String query[] = keyword[1].split("&searchtype");
                        intent.putExtra("query", query[0]);
                        startActivity(intent);
                    }

                }
            });
            tv_their_search_5.setText(homeItems.get(7).getBookName());
            tv_their_search_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    if (homeItems.get(7).getBookLink() != null) {
                        String keyword[] = homeItems.get(7).getBookLink().split("kw=");
                        String query[] = keyword[1].split("&searchtype");
                        intent.putExtra("query", query[0]);
                        startActivity(intent);
                    }

                }
            });
            tv_their_search_6.setText(homeItems.get(8).getBookName());
            tv_their_search_6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    if (homeItems.get(8).getBookLink() != null) {
                        String keyword[] = homeItems.get(8).getBookLink().split("kw=");
                        String query[] = keyword[1].split("&searchtype");
                        intent.putExtra("query", query[0]);
                        startActivity(intent);
                    }

                }
            });
            tv_their_search_7.setText(homeItems.get(9).getBookName());
            tv_their_search_7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    if (homeItems.get(9).getBookLink() != null) {
                        String keyword[] = homeItems.get(9).getBookLink().split("kw=");
                        String query[] = keyword[1].split("&searchtype");
                        intent.putExtra("query", query[0]);
                        startActivity(intent);
                    }

                }
            });
            tv_their_search_8.setText(homeItems.get(10).getBookName());
            tv_their_search_8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    if (homeItems.get(10).getBookLink() != null) {
                        String keyword[] = homeItems.get(10).getBookLink().split("kw=");
                        String query[] = keyword[1].split("&searchtype");
                        intent.putExtra("query", query[0]);
                        startActivity(intent);
                    }

                }
            });
            tv_their_search_9.setText(homeItems.get(11).getBookName());
            tv_their_search_9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
                    if (homeItems.get(11).getBookLink() != null) {
                        String keyword[] = homeItems.get(11).getBookLink().split("kw=");
                        String query[] = keyword[1].split("&searchtype");
                        intent.putExtra("query", query[0]);
                        startActivity(intent);
                    }

                }
            });
            super.onPostExecute(homeItems);

            progressBar.setVisibility(View.GONE);
            progressBar1.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
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
