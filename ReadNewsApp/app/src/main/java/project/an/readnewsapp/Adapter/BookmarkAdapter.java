package project.an.readnewsapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import project.an.readnewsapp.Activity.NewsDetailActivity;
import project.an.readnewsapp.Models.NewsItem;
import project.an.readnewsapp.R;
import project.an.readnewsapp.Service.DatabaseHelper;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.BookmarkViewHolder>{

    private final Context context;
    private final DatabaseHelper databaseHelper;
    private final List<NewsItem> bookmarList;

    public BookmarkAdapter(Context context, List<NewsItem> newsItems) {
        this.context = context;
        databaseHelper = DatabaseHelper.getInstance(this.context);
        bookmarList = newsItems;
    }

    @NonNull
    @Override
    public BookmarkAdapter.BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_news_bookmark, parent, false);
        return new BookmarkAdapter.BookmarkViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkAdapter.BookmarkViewHolder holder, int position) {
        NewsItem newsItem = bookmarList.get(position);
        holder.titleBookmarkList.setText(newsItem.getTitle());
        holder.categoryBookmarkList.setText(newsItem.getCategory());
        if(newsItem.getImgUrl() == null) Log.i("bookmark", "Không nhận được hình ảnh");
        else Log.i("bookmark", newsItem.getImgUrl());
        Glide.with(context)
                .load(newsItem.getImgUrl())
                .placeholder(R.drawable.place_holder)
                .into(holder.imageNewsBookmarkList);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("title", newsItem.getTitle());
            intent.putExtra("imageUrl", newsItem.getImgUrl());
            intent.putExtra("link", newsItem.getLink());
            intent.putExtra("content", newsItem.getContent());
            intent.putExtra("pubDate", newsItem.getPupDate());
            intent.putExtra("category", newsItem.getCategory());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bookmarList.size();
    }

    static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        ImageView imageNewsBookmarkList;
        TextView categoryBookmarkList, titleBookmarkList;

        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            imageNewsBookmarkList = itemView.findViewById(R.id.imageNewsBookmarkList);
            categoryBookmarkList = itemView.findViewById(R.id.categoryBookmarkList);
            titleBookmarkList = itemView.findViewById(R.id.titleBookmarkList);
        }
    }
}
