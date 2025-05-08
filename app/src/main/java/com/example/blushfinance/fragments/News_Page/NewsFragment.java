package com.example.blushfinance.fragments.News_Page;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import android.os.Bundle;
import com.example.blushfinance.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.request.SourcesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class NewsFragment extends Fragment {

    private static final String TAG = "NewsFragment";
    private static final String API_KEY = "f1f8432c6626431f89f2b3993d9db3b8";

    private RecyclerView recyclerView;

    private List<Article> articleList = new ArrayList<>();
    private NewsRecyclerAdapter adapter;
    private LinearProgressIndicator progressIndicator;

    public NewsFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.news_recycler_view);
        progressIndicator = view.findViewById(R.id.progress_bar);

        setupRecyclerView();
        getNewsEverything();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }

    private void changeInProgress(boolean show) {
        if (progressIndicator != null && isAdded()) {
            progressIndicator.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        }

    }

    private void getNewsEverything() {
        changeInProgress(true);
        Log.d(TAG, "Attempting to fetch news using 'everything' endpoint");

        try {
            NewsApiClient newsApiClient = new NewsApiClient(API_KEY);

            EverythingRequest request = new EverythingRequest.Builder()
                    .q("Business")
                    .language("en")
                    .sortBy("publishedAt")
                    .build();
            newsApiClient.getEverything(request, new NewsApiClient.ArticlesResponseCallback() {
                @Override
                public void onSuccess(ArticleResponse response) {
                    handleSuccessResponse(response);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.e(TAG, "Everything endpoint failed", throwable);
                    // If the "everything" endpoint fails, try a direct HTTP request
                    handleFailure(throwable);
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "Exception setting up everything request", e);
            onAPIError("Error: " + e.getMessage());
        }
    }

    private void handleSuccessResponse(ArticleResponse response) {
        if (getActivity() == null || !isAdded()) {
            return;
        }

        getActivity().runOnUiThread(() -> {
            if (response.getArticles() != null && !response.getArticles().isEmpty()) {
                Log.d(TAG, "Received " + response.getArticles().size() + " articles");
                articleList.clear();
                articleList.addAll(response.getArticles());
                adapter.notifyDataSetChanged();


                // Log first article for debugging
                if (!response.getArticles().isEmpty()) {
                    Article firstArticle = response.getArticles().get(0);
                    Log.d(TAG, "First article: " + firstArticle.getTitle());
                    Log.d(TAG, "Source: " + (firstArticle.getSource() != null ?
                            firstArticle.getSource().getName() : "Unknown"));
                }
            } else {
                Log.w(TAG, "No articles found in the response");
                Toast.makeText(getContext(), "No news articles found", Toast.LENGTH_SHORT).show();
            }
            changeInProgress(false);
        });
    }


    private void handleFailure(Throwable throwable) {
        String errorMessage = throwable.getMessage();
        Log.e(TAG, "API request failed: " + errorMessage, throwable);

        // Check if this is the HTML doctype error
        if (errorMessage != null && errorMessage.contains("<!DOCTYPE")) {
            Log.e(TAG, "Received HTML instead of JSON. This likely indicates an authentication issue.");
            onAPIError("API authentication error. Please check your API key.");
        } else {
            onAPIError("Failed to load news: " + errorMessage);
        }
    }

    private void onAPIError(String message) {
        if (getActivity() == null || !isAdded()) {
            return;
        }

        getActivity().runOnUiThread(() -> {
            changeInProgress(false);
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        });
    }
}





