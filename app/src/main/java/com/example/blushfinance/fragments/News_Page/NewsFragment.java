package com.example.blushfinance.fragments.News_Page;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Bundle;
import com.example.blushfinance.R;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;

public class NewsFragment extends Fragment {

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
        getNews();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }

    private void changeInProgress(boolean show) {
        progressIndicator.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void getNews() {
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("f1f8432c6626431f89f2b3993d9db3b8"); // Use your actual API key
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        if (isAdded()) { // Check if fragment is currently added to its activity
                            getActivity().runOnUiThread(() -> {
                                changeInProgress(false);
                                articleList = response.getArticles();
                                adapter.updateData(articleList);
                                adapter.notifyDataSetChanged();
                            });
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("API Failure", throwable.getMessage());
                    }
                }
        );
    }
}