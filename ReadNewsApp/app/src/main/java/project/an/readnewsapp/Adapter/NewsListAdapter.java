package project.an.readnewsapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import project.an.readnewsapp.Activity.NewsDetailActivity;
import project.an.readnewsapp.Models.NewsItem;
import project.an.readnewsapp.R;
import project.an.readnewsapp.Service.DatabaseHelper;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder>  {

    private final List<NewsItem> newsItems;
    private final Context context;
    private final String categoryName;
    private final DatabaseHelper databaseHelper;

    public NewsListAdapter(List<NewsItem> rssItems, Context context, String category) {
        this.newsItems = rssItems;
        this.context = context;
        this.categoryName = category;
        databaseHelper = DatabaseHelper.getInstance(this.context);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsItem newsItem = newsItems.get(position);
        boolean isBookmarked = databaseHelper.isBookmarked(newsItem.getTitle());
        if(isBookmarked) holder.bookmark.setImageResource(R.drawable.icon_bookmark_chosen);
        else holder.bookmark.setImageResource(R.drawable.icon_bookmark);
        holder.titleTextView.setText(newsItem.getTitle());
//         Hiển thị hình ảnh bằng Glide (thư viện tải ảnh)
        Glide.with(context)
                .load(newsItem.getImgUrl())
                .placeholder(R.drawable.place_holder) // Hình ảnh thay thế khi đang tải
                .into(holder.imageView);
//         Bắt sự kiện click vào một item
        holder.bookmark.setOnClickListener(v -> {
            boolean isBookmarkedcheck = databaseHelper.isBookmarked(newsItem.getTitle());
            if (isBookmarkedcheck) {
                // Xóa khỏi bookmark
                int rowsDeleted = databaseHelper.deleteBookmark(newsItem.getTitle());
                if (rowsDeleted > 0) {

                    Toast.makeText(context, "Đã xóa khỏi bookmark", Toast.LENGTH_SHORT).show();
                    holder.bookmark.setImageResource(R.drawable.icon_bookmark);
                } else {
                    Toast.makeText(context, "Lỗi khi xóa bookmark", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Thêm vào bookmark
                long result = databaseHelper.insertBookmark(
                        newsItem.getTitle(),
                        newsItem.getLink(),
                        newsItem.getImgUrl(),
                        newsItem.getContent(),
                        newsItem.getPupDate(),
                        categoryName
                );
                if (result > 0) {
                    Toast.makeText(context, "Đã thêm vào bookmark", Toast.LENGTH_SHORT).show();
                    holder.bookmark.setImageResource(R.drawable.icon_bookmark_chosen);
                }
                else Toast.makeText(context, "Lỗi khi thêm bookmark", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("title", newsItem.getTitle());
            intent.putExtra("imageUrl", newsItem.getImgUrl());
            intent.putExtra("link", newsItem.getLink());
            intent.putExtra("content", newsItem.getContent());
            intent.putExtra("pubDate", newsItem.getPupDate());
            intent.putExtra("category", categoryName);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;
        ImageView bookmark;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleNews);
            imageView = itemView.findViewById(R.id.imageNews);
            bookmark = itemView.findViewById(R.id.bookMarkItem);
        }
    }

    public void updateData(List<NewsItem> newNewsList) {
        this.newsItems.clear();
        this.newsItems.addAll(newNewsList);
        notifyDataSetChanged(); // Làm mới RecyclerView
    }



}
