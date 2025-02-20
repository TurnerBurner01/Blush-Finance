package com.example.blushfinance.NewsPage;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import com.example.blushfinance.NewsPage.Article;

import com.example.blushfinance.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    // Change the List type to Article since we are using Article now
    private List<Article> newsItems;

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        public ImageView newsImage;
        public TextView newsCaption;

        public NewsViewHolder(View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.news_image);
            newsCaption = itemView.findViewById(R.id.news_caption);
        }
    }

    // Constructor now correctly receives a List<Article>
    public NewsAdapter(List<Article> newsItems) {
        this.newsItems = newsItems;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Make sure the layout being inflated is correct
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NewsAdapter.NewsViewHolder holder, int position) {
        // Correctly referencing the list of Article objects
        Article article = newsItems.get(position);
        holder.newsCaption.setText(article.getDescription());
        // Assuming Glide is used to load images, and the ImageView id is correct
        // Uncomment and use this if Glide is setup
        // Glide.with(holder.itemView.getContext()).load(article.getUrlToImage()).into(holder.newsImage);
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }
}
