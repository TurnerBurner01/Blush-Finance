package com.example.blushfinance.fragments.News_Page;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;

import com.example.blushfinance.R;

public class News_Page extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<Article> newsList = new ArrayList<>(); // Use Article here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news); // Make sure this contains the RecyclerView

        recyclerView = findViewById(R.id.news_recycler_view); // Check this ID in your layout
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new NewsAdapter(newsList);
            recyclerView.setAdapter(adapter);
        }

        loadNews(); // Call loadNews here
    }

    private void loadNews() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<NewsResponse> call = apiService.getTopHeadlines("business", "your_api_key_here");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newsList.clear();
                    newsList.addAll(response.body().getArticles()); // Assuming getArticles returns List<Article>
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                Toast.makeText(News_Page.this, "Failed to fetch news", Toast.LENGTH_SHORT).show();
            }
        });
    }
}