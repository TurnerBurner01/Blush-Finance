package com.example.blushfinance.fragments.Home_Page;

import static com.example.blushfinance.fragments.News_Page.NewsFragment.API_KEY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.TextView;

import com.example.blushfinance.R;
import com.example.blushfinance.db.ExpenseCard;
import com.example.blushfinance.db.IncomeCard;
import com.example.blushfinance.db.Transaction;
import com.example.blushfinance.fragments.Finance_Page.CustomValueFormatter;
import com.example.blushfinance.fragments.Finance_Page.ExpenseViewModel;
import com.example.blushfinance.fragments.Finance_Page.IncomeViewModel;
import com.example.blushfinance.fragments.News_Page.NewsRecyclerAdapter;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
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

    private RecyclerView recentRecycler;
    private RecentTransactionsAdapter recentAdapter;
    private final List<Transaction> recentList = new ArrayList<>();
    private List<IncomeCard> incomeSnapshot = new ArrayList<>();
    private List<ExpenseCard> expenseSnapshot = new ArrayList<>();

    private PieChart homePieChart;
    private Button btnShowExpenses, btnShowIncome;

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
                        LinearLayoutManager.VERTICAL,
                        false));
        homeNewsAdapter = new NewsRecyclerAdapter(homeArticleList);
        homeNewsRecycler.setAdapter(homeNewsAdapter);
        fetchRandomNews();

        // Recycler setup
        recentRecycler = view.findViewById(R.id.recent_transactions_recycler);
        recentRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recentAdapter = new RecentTransactionsAdapter(recentList);
        recentRecycler.setAdapter(recentAdapter);

        // Pie chart and button IDs
        homePieChart = view.findViewById(R.id.home_pie_chart);
        btnShowExpenses = view.findViewById(R.id.btn_show_expenses);
        btnShowIncome = view.findViewById(R.id.btn_show_income);

        // Initialize ViewModels, observers in onViewCreated
        incomeVM = new ViewModelProvider(requireActivity())
                .get(IncomeViewModel.class);
        expenseVM = new ViewModelProvider(requireActivity())
                .get(ExpenseViewModel.class);

        // Chart styling
        homePieChart.setUsePercentValues(true);
        homePieChart.setDrawHoleEnabled(true);
        homePieChart.setHoleColor(Color.WHITE);
        homePieChart.setTransparentCircleColor(Color.parseColor("#FAFAFA"));
        homePieChart.setTransparentCircleAlpha(100);
        homePieChart.setCenterTextSize(16f);
        homePieChart.setDrawCenterText(true);
        homePieChart.setHighlightPerTapEnabled(true);
        homePieChart.getDescription().setEnabled(false);
        homePieChart.getLegend().setEnabled(false);
        homePieChart.setEntryLabelColor(Color.DKGRAY);
        homePieChart.setEntryLabelTextSize(10f);
        homePieChart.setRotationEnabled(false);
        homePieChart.setNoDataText("No data yet.");

        // Setup buttons
        btnShowExpenses.setOnClickListener(v -> {
            btnShowExpenses.setSelected(true);
            btnShowIncome.setSelected(false);
            showExpensesChart();
        });

        btnShowIncome.setOnClickListener(v -> {
            btnShowExpenses.setSelected(false);
            btnShowIncome.setSelected(true);
            showIncomeChart();
        });

        // Default pie chart
        btnShowExpenses.setSelected(true);
        btnShowIncome.setSelected(false);
        showExpensesChart();

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observe income
        incomeVM.getActualCardsLiveData()
                .observe(getViewLifecycleOwner(), list -> {
                    float sum = 0f;
                    incomeSnapshot.clear();
                    if(list != null) {
                        incomeSnapshot.addAll(list);
                        for (IncomeCard c : list) sum += c.getAmount();
                    }
                    totalIncome = sum;
                    updateRecentTransaction();
                    updateBalanceUi();
                });

        // Observe expenses
        expenseVM.getActualCardsLiveData()
                .observe(getViewLifecycleOwner(), list -> {
                    float sum = 0f;
                    expenseSnapshot.clear();
                    if (list != null) {
                        expenseSnapshot.addAll(list);
                        for (ExpenseCard c : list) sum += c.getAmount();
                    }
                    totalExpense = sum;
                    updateRecentTransaction();
                    updateBalanceUi();
                });
    }

    private void showExpensesChart() {
        expenseVM.getActualCardsLiveData()
                .observe(getViewLifecycleOwner(), cards -> {
                    if (cards == null || cards.isEmpty()) {
                        homePieChart.clear();
                        homePieChart.invalidate();
                        return;
                    }
                    // build entries & colors (same as loadExpensePieChartData) :contentReference[oaicite:1]{index=1}:contentReference[oaicite:2]{index=2}
                    ArrayList<PieEntry>    entries     = new ArrayList<>();
                    ArrayList<Integer>     sliceColors = new ArrayList<>();
                    for (ExpenseCard c : cards) {
                        entries.add(new PieEntry(c.getAmount(), c.getName()));
                        sliceColors.add(c.getItemColor());
                    }
                    PieDataSet dataSet = new PieDataSet(entries, "Expenses");
                    dataSet.setColors(sliceColors);
                    dataSet.setValueTextSize(10f);
                    dataSet.setSliceSpace(2f);
                    dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

                    PieData data = new PieData(dataSet);
                    data.setValueFormatter(new CustomValueFormatter(homePieChart));  // :contentReference[oaicite:3]{index=3}:contentReference[oaicite:4]{index=4}
                    data.setValueTextColor(Color.DKGRAY);

                    homePieChart.setData(data);
                    // center text total (same as updateTotalExpensesUI)
                    float total = 0f;
                    for (ExpenseCard c : cards) total += c.getAmount();
                    homePieChart.setCenterText(
                            String.format(Locale.UK, "Total: £%.0f", total));
                    homePieChart.invalidate();
                    homePieChart.animateY(1000, Easing.EaseInOutQuad);
                });
    }

    private void showIncomeChart() {
        incomeVM.getActualCardsLiveData()
                .observe(getViewLifecycleOwner(), cards -> {
                    if (cards == null || cards.isEmpty()) {
                        homePieChart.clear();
                        homePieChart.invalidate();
                        return;
                    }
                    ArrayList<PieEntry>    entries     = new ArrayList<>();
                    ArrayList<Integer>     sliceColors = new ArrayList<>();
                    for (IncomeCard c : cards) {
                        entries.add(new PieEntry(c.getAmount(), c.getName()));
                        sliceColors.add(c.getItemColor());
                    }
                    PieDataSet dataSet = new PieDataSet(entries, "Income");
                    dataSet.setColors(sliceColors);
                    dataSet.setValueTextSize(10f);
                    dataSet.setSliceSpace(2f);
                    dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

                    PieData data = new PieData(dataSet);
                    data.setValueFormatter(new CustomValueFormatter(homePieChart));
                    data.setValueTextColor(Color.DKGRAY);

                    homePieChart.setData(data);
                    float total = 0f;
                    for (IncomeCard c : cards) total += c.getAmount();
                    homePieChart.setCenterText(
                            String.format(Locale.UK, "Total: £%.0f", total));
                    homePieChart.invalidate();
                    homePieChart.animateY(1000, Easing.EaseInOutQuad);
                });
    }

    private void updateRecentTransaction() {
        List<Transaction> all = new ArrayList<>();
        for (IncomeCard ic : incomeSnapshot) {
            all.add(new Transaction(ic.getId(),
                                    ic.getName(),
                                    ic.getAmount(),
                                    true));
        }
        for (ExpenseCard ec : expenseSnapshot) {
            all.add(new Transaction(ec.getId(),
                                    ec.getName(),
                                    ec.getAmount(),
                                    false));
        }

        Collections.sort(all, (a, b) -> b.id - a.id);

        List<Transaction> top3 = all.subList(0, Math.min(3, all.size()));

        recentList.clear();
        recentList.addAll(top3);
        recentAdapter.notifyDataSetChanged();
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
