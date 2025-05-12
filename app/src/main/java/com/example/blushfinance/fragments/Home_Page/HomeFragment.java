package com.example.blushfinance.fragments.Home_Page;

import static com.example.blushfinance.fragments.News_Page.NewsFragment.API_KEY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blushfinance.R;
import com.example.blushfinance.fragments.News_Page.NewsRecyclerAdapter;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// admin1
// Q1234567

public class HomeFragment extends Fragment {

    private NewsRecyclerAdapter homeNewsAdapter;
    private final List<Article> homeArticleList = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView welcome = view.findViewById(R.id.welcome);

        super.onViewCreated(view, savedInstanceState);

        RecyclerView homeNewsRecycler = view.findViewById(R.id.home_news_recycler);
        homeNewsRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false));

        homeNewsAdapter = new NewsRecyclerAdapter(homeArticleList);
        homeNewsRecycler.setAdapter(homeNewsAdapter);

        fetchRandomNews();

        String user = requireActivity()
                .getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .getString("username", "User");

        welcome.setText("Welcome " + user + ",");
        return view;
    }

    public void fetchRandomNews() {
        NewsApiClient client = new NewsApiClient(API_KEY);
        EverythingRequest req = new EverythingRequest.Builder()
                .q("Business")
                .language("en")
                .sortBy("publishedAt")
                .build();

        client.getEverything(req, new NewsApiClient.ArticlesResponseCallback() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(ArticleResponse response) {
                if (getActivity() == null || !isAdded()) return;
                getActivity().runOnUiThread(() -> {
                    List<Article> all = response.getArticles();
                    if (all == null || all.isEmpty()) return;
                    // Pick 2 articles randomly
                    Collections.shuffle(all);
                    List<Article> subset = all.subList(0, Math.min(4, all.size()));
                    homeArticleList.clear();
                    homeArticleList.addAll(subset);
                    homeNewsAdapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("HomeFragment", "News load failure", t);
            }
        });
    }
}
