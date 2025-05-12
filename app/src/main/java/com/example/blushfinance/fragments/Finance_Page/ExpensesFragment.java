package com.example.blushfinance.fragments.Finance_Page;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider; // For ViewModel
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blushfinance.R;
// Import your entity from the 'db' package
import com.example.blushfinance.db.ExpenseCard; // *** IMPORTANT: Ensure this path is correct ***
// Import your ViewModel (ensure it's in the correct package)
import com.example.blushfinance.fragments.Finance_Page.ExpenseViewModel; // *** IMPORTANT: Ensure this path is correct ***

// Ensure other imports for Adapter, Dialog, Formatter are correct
// import com.example.blushfinance.fragments.Finance_Page.ExpenseCardAdapter;
// import com.example.blushfinance.fragments.Finance_Page.AddExpenseDialogFragment;
// import com.example.blushfinance.fragments.Finance_Page.CustomValueFormatter;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Fragment to display expense data using a PieChart and RecyclerView.
 * Data is managed by ExpenseViewModel and persisted using Room.
 */
public class ExpensesFragment extends Fragment implements AddExpenseDialogFragment.AddExpenseDialogListener {

    private static final String TAG = "ExpensesFragment";

    // UI Elements
    private PieChart expensePieChart;
    private RecyclerView expenseRecyclerView;

    // Adapter and ViewModel
    private ExpenseCardAdapter expenseCardAdapter; // Ensure ExpenseCardAdapter is created and imported
    private ExpenseViewModel expenseViewModel;     // Instance of ExpenseViewModel

    // Color management for expense categories
    private List<Integer> expenseColorPalette;
    private int currentExpenseColorIndex = 0;

    public ExpensesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModel
        // *** Ensure ExpenseViewModel class exists and is imported correctly ***
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);

        // Define your color palette for expenses
        expenseColorPalette = new ArrayList<>(Arrays.asList(
                Color.parseColor("#EF5350"), // Red 400
                Color.parseColor("#FFA726"), // Orange 400
                Color.parseColor("#FFEE58"), // Yellow 400 (ensure dark text on this color)
                Color.parseColor("#EC407A"), // Pink 400
                Color.parseColor("#AB47BC"), // Purple 400
                Color.parseColor("#7E57C2"), // Deep Purple 400
                Color.parseColor("#5C6BC0"), // Indigo 400
                Color.parseColor("#42A5F5")  // Blue 400
        ));
        currentExpenseColorIndex = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);

        // Use the IDs from your fragment_expenses.xml
        expensePieChart = view.findViewById(R.id.pie_chart); // Assuming you used pie_chart ID
        expenseRecyclerView = view.findViewById(R.id.recyclerView); // Assuming you used recyclerView ID

        setupExpensePieChart();
        expenseRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Observe the list for the RecyclerView adapter
        // *** Ensure ExpenseViewModel has getDisplayableExpenseCardsList() method ***
        expenseViewModel.getDisplayableExpenseCardsList().observe(getViewLifecycleOwner(), displayCards -> {
            Log.d(TAG, "Observer: Displayable expense cards updated, count: " + (displayCards != null ? displayCards.size() : "null"));
            if (displayCards == null) {
                Log.e(TAG, "displayCards list is null in observer!");
                return;
            }
            if (expenseCardAdapter == null) {
                // *** Ensure ExpenseCardAdapter class exists and its constructor matches ***
                expenseCardAdapter = new ExpenseCardAdapter(displayCards, getContext(), this);
                expenseRecyclerView.setAdapter(expenseCardAdapter);
            } else {
                // *** Ensure ExpenseCardAdapter has updateData(List<ExpenseCard from db package>) ***
                expenseCardAdapter.updateData(displayCards);
            }
        });

        // Observe the actual cards (from database) for pie chart and total calculation
        // *** Ensure ExpenseViewModel has getActualCardsLiveData() method ***
        expenseViewModel.getActualCardsLiveData().observe(getViewLifecycleOwner(), actualCards -> {
            Log.d(TAG, "Observer: Actual expense cards from DB updated, count: " + (actualCards != null ? actualCards.size() : "null"));
            if (actualCards != null) {
                loadExpensePieChartData(actualCards);
                updateTotalExpensesUI(actualCards);
            }
        });

        return view;
    }

    private void setupExpensePieChart() {
        if (expensePieChart == null) {
            Log.e(TAG, "setupExpensePieChart: expensePieChart is null!");
            return;
        }
        expensePieChart.setUsePercentValues(true);
        expensePieChart.setDrawHoleEnabled(true);
        expensePieChart.setHoleColor(Color.parseColor("#FFFFFF"));
        expensePieChart.setTransparentCircleColor(Color.parseColor("#FAFAFA"));
        expensePieChart.setTransparentCircleAlpha(100);
        expensePieChart.setCenterTextSize(16f);
        expensePieChart.setDrawCenterText(true);
        expensePieChart.setHighlightPerTapEnabled(true);
        expensePieChart.getDescription().setEnabled(false);
        expensePieChart.getLegend().setEnabled(false);
        expensePieChart.setEntryLabelColor(Color.DKGRAY);
        expensePieChart.setEntryLabelTextSize(10f);
        expensePieChart.setRotationEnabled(false);
        expensePieChart.setNoDataText("No expense data yet.");
    }

    private void loadExpensePieChartData(List<ExpenseCard> actualExpenseCards) {
        if (expensePieChart == null) {
            Log.e(TAG, "loadExpensePieChartData: PieChart is null.");
            return;
        }
        Log.d(TAG, "loadExpensePieChartData. Actual expense cards: " + (actualExpenseCards != null ? actualExpenseCards.size() : "null list"));

        if (actualExpenseCards == null || actualExpenseCards.isEmpty()) {
            expensePieChart.clear();
            expensePieChart.invalidate(); // Shows "No data text" set in setup
            return;
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> sliceColors = new ArrayList<>();

        for (ExpenseCard card : actualExpenseCards) {
            // card here is com.example.blushfinance.db.ExpenseCard
            entries.add(new PieEntry(card.getAmount(), card.getName()));
            sliceColors.add(card.getItemColor());
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
        dataSet.setColors(sliceColors);
        dataSet.setValueTextSize(10f);
        dataSet.setSliceSpace(2f);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

        PieData data = new PieData(dataSet);
        if (getContext() != null) {
            // *** Ensure CustomValueFormatter class exists and is imported ***
            data.setValueFormatter(new CustomValueFormatter(expensePieChart));
        }
        data.setValueTextColor(Color.DKGRAY);

        expensePieChart.setData(data);
        // Total will be updated by updateTotalExpensesUI, which is also observing actualCards
        expensePieChart.invalidate();
        expensePieChart.animateY(1000, Easing.EaseInOutQuad);
    }

    private void updateTotalExpensesUI(List<ExpenseCard> actualExpenseCards) {
        float total = 0;
        if (actualExpenseCards != null) {
            for (ExpenseCard card : actualExpenseCards) {
                // card here is com.example.blushfinance.db.ExpenseCard
                total += card.getAmount();
            }
        }
        String totalText = String.format(Locale.UK, "Total Expenses: £%.2f", total);
        if (expensePieChart != null) {
            expensePieChart.setCenterText(totalText);
        }
    }

    private int getNextExpenseColor() {
        if (expenseColorPalette == null || expenseColorPalette.isEmpty()) {
            return Color.GRAY;
        }
        List<ExpenseCard> currentActualCards = expenseViewModel.getActualCardsLiveData().getValue();
        int nextIndex = (currentActualCards != null ? currentActualCards.size() : 0) % expenseColorPalette.size();
        return expenseColorPalette.get(nextIndex);
    }

    @Override
    public void onExpenseAdded(String name, float amount) {
        Log.d(TAG, "New expense received from dialog: " + name + ", Amount: " + amount);

        int color = getNextExpenseColor();
        // *** Ensure ExpenseCard (from db package) constructor is (String, float, int) ***
        ExpenseCard newExpenseCard = new ExpenseCard(name, amount, color);

        // *** Ensure ExpenseViewModel has addExpenseCard(ExpenseCard from db package) method ***
        expenseViewModel.addExpenseCard(newExpenseCard);
        // LiveData observers will handle UI updates.
    }

    public void showAddExpenseDialog() {
        // *** Ensure AddExpenseDialogFragment exists, defines AddExpenseDialogListener, and has newInstance(listener) ***
        AddExpenseDialogFragment dialog = AddExpenseDialogFragment.newInstance(this);
        dialog.show(getParentFragmentManager(), "AddExpenseDialogFragmentTag");
    }
}
