package com.example.blushfinance.fragments.Home_Page;

import static com.example.blushfinance.fragments.News_Page.NewsFragment.API_KEY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.blushfinance.R;
import com.example.blushfinance.db.ExpenseCard;
import com.example.blushfinance.db.IncomeCard;
import com.example.blushfinance.fragments.Finance_Page.ExpenseViewModel;
import com.example.blushfinance.fragments.Finance_Page.IncomeViewModel;
import com.example.blushfinance.fragments.News_Page.NewsRecyclerAdapter;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

// admin1
// Q1234567

public class HomeFragment extends Fragment {

    private NewsRecyclerAdapter homeNewsAdapter;
    private final List<Article> homeArticleList = new ArrayList<>();

    private IncomeViewModel incomeVM;
    private ExpenseViewModel expenseVM;
    private TextView balanceTv;
    private float totalIncome = 0f;
    private float totalExpense = 0f;

    public HomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Dynamic user greeting
        TextView welcome = view.findViewById(R.id.welcome);
        String user = requireActivity()
                .getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                .getString("username", "User");
        welcome.setText("Welcome " + user + ",");

        balanceTv = view.findViewById(R.id.txt_balance_amount);

        // News display
        RecyclerView homeNewsRecycler = view.findViewById(R.id.home_news_recycler);
        homeNewsRecycler.setLayoutManager(
                new LinearLayoutManager(getContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false));
        homeNewsAdapter = new NewsRecyclerAdapter(homeArticleList);
        homeNewsRecycler.setAdapter(homeNewsAdapter);
        fetchRandomNews();

        // Initialize ViewModels, observers in onViewCreated
        incomeVM = new ViewModelProvider(requireActivity())
                .get(IncomeViewModel.class);
        expenseVM = new ViewModelProvider(requireActivity())
                .get(ExpenseViewModel.class);

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observe income
        incomeVM.getActualCardsLiveData()
                .observe(getViewLifecycleOwner(), list -> {
                    float sum = 0f;
                    if(list != null) {
                        for (IncomeCard c : list) sum += c.getAmount();
                    }
                    totalIncome = sum;
                    updateBalanceUi();
                });

        // Observe expenses
        expenseVM.getActualCardsLiveData()
                .observe(getViewLifecycleOwner(), list -> {
                    float sum = 0f;
                    if (list != null) {
                        for (ExpenseCard c : list) sum += c.getAmount();
                    }
                    totalExpense = sum;
                    updateBalanceUi();
                });
    }

    private void updateBalanceUi() {
        float balance = totalIncome - totalExpense;
        balanceTv.setText(String.format(Locale.UK, "£%.0f", balance));
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
