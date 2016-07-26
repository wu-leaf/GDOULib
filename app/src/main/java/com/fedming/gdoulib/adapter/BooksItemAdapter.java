package com.fedming.gdoulib.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fedming.gdoulib.R;
import com.fedming.gdoulib.bean.BooksItem;

import java.util.ArrayList;
import java.util.List;

public class BooksItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    private List<BooksItem> mBooksList = new ArrayList<>();

    public List<BooksItem> getmBooksList() {
        return mBooksList;
    }

    public BooksItemAdapter(Context context) {
        this(context, null);
    }

    public BooksItemAdapter(Context context, List<BooksItem> myDataset) {
        mBooksList = myDataset != null ? myDataset : new ArrayList<BooksItem>();
    }

    public void addBooks(List<BooksItem> books) {

        mBooksList.addAll(books);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_book_item, parent, false);

            TextView tv_book_name = (TextView) view.findViewById(R.id.book_name);
            TextView tv_book_info = (TextView) view.findViewById(R.id.book_info);
            return new ItemViewHolder(view, tv_book_name, tv_book_info);

        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_footerview, parent, false);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            view.setClickable(false);
            return new FooterViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ItemViewHolder){

            ((ItemViewHolder) holder).tv_book_name.setText(mBooksList.get(position).getBookName());
            ((ItemViewHolder) holder).tv_book_info.setText(mBooksList.get(position).getBookInfo());

            ((ItemViewHolder) holder).bindData(mBooksList.get(position));
        }

    }

    @Override
    public int getItemCount() {
        return mBooksList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
            view.setClickable(false);
        }

    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_book_name;
        private TextView tv_book_info;


        public ItemViewHolder(View itemView, TextView bookNameTextView, TextView bookInfoTextView) {
            super(itemView);
//            itemView.setOnClickListener(this);
            tv_book_name = bookNameTextView;
            tv_book_info = bookInfoTextView;
        }

        public void bindData(BooksItem booksItem) {
            tv_book_name.setText(booksItem.getBookName());
            tv_book_info.setText(booksItem.getBookInfo());
        }

    }
}
