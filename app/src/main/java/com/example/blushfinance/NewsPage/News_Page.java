package com.example.blushfinance.NewsPage;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.widget.Toast;
import com.example.blushfinance.NewsPage.NewsResponse;
import com.example.blushfinance.NewsPage.Article;

import com.example.blushfinance.R;

public class News_Page extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private List<NewsItem> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.news_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(newsList);
        recyclerView.setAdapter(adapter);

        loadNews();  // Call loadNews here
    }

    private void loadNews() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<NewsResponse> call = apiService.getTopHeadlines("business", "f1f8432c6626431f89f2b3993d9db3b8");

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    newsList.clear();
                    newsList.addAll(response.body().getArticles()); // Assuming NewsResponse has a getArticles method
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