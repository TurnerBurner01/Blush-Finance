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
import com.example.blushfinance.db.IncomeCard; // *** IMPORTANT: Ensure this path is correct ***
// Import your ViewModel (assuming it's in the same package as this fragment for now)
import com.example.blushfinance.fragments.Finance_Page.IncomeViewModel; // *** IMPORTANT: Ensure this path is correct ***

// Import your Adapter (assuming it's in the same package)
// import com.example.blushfinance.fragments.Finance_Page.IncomeCardAdapter;
// Import your Dialog (assuming it's in the same package)
// import com.example.blushfinance.fragments.Finance_Page.AddPotDialogFragment;
// Import your Formatter (assuming it's in the same package)
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
 * Fragment to display income data using a PieChart and RecyclerView.
 * Data is managed by IncomeViewModel and persisted using Room.
 */
public class IncomeFragment extends Fragment implements AddPotDialogFragment.AddPotDialogListener {

    private static final String TAG = "IncomeFragment";

    // UI Elements
    private PieChart incomePieChart;
    private RecyclerView incomeRecyclerView;

    // Adapter and ViewModel
    private IncomeCardAdapter incomeCardAdapter; // Ensure IncomeCardAdapter is created and imported
    private IncomeViewModel incomeViewModel;     // Instance of IncomeViewModel

    // Color management for income categories
    private List<Integer> incomeColorPalette;
    private int currentIncomeColorIndex = 0;

    public IncomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize ViewModel
        // *** Ensure IncomeViewModel class exists and is imported correctly ***
        incomeViewModel = new ViewModelProvider(this).get(IncomeViewModel.class);

        // Define your color palette for income
        incomeColorPalette = new ArrayList<>(Arrays.asList(
                Color.parseColor("#66BB6A"), // Green 400
                Color.parseColor("#42A5F5"), // Blue 400
                Color.parseColor("#FFEE58"), // Yellow 400 (ensure dark text on this color)
                Color.parseColor("#26A69A"), // Teal 400
                Color.parseColor("#AB47BC"), // Purple 400
                Color.parseColor("#7E57C2"), // Deep Purple 400
                Color.parseColor("#FFA726"), // Orange 400
                Color.parseColor("#8D6E63")  // Brown 400
        ));
        currentIncomeColorIndex = 0;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        incomePieChart = view.findViewById(R.id.pie_chart); // Ensure this ID is in fragment_income.xml
        incomeRecyclerView = view.findViewById(R.id.recyclerView); // Ensure this ID is in fragment_income.xml

        setupIncomePieChart();
        incomeRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Observe the list for the RecyclerView adapter
        // *** Ensure IncomeViewModel has getDisplayableIncomeCardsList() method returning LiveData<List<IncomeCard>> ***
        // *** Where IncomeCard is com.example.blushfinance.db.IncomeCard ***
        incomeViewModel.getDisplayableIncomeCardsList().observe(getViewLifecycleOwner(), displayCards -> {
            Log.d(TAG, "Observer: Displayable income cards updated, count: " + (displayCards != null ? displayCards.size() : "null"));
            if (displayCards == null) { // Safety check for the list itself
                Log.e(TAG, "displayCards list is null in observer!");
                return;
            }
            if (incomeCardAdapter == null) {
                // *** Ensure IncomeCardAdapter exists and its constructor matches:
                // (List<IncomeCard> from db package, Context, IncomeFragment) ***
                incomeCardAdapter = new IncomeCardAdapter(displayCards, getContext(), this);
                incomeRecyclerView.setAdapter(incomeCardAdapter);
            } else {
                // *** Ensure IncomeCardAdapter has updateData(List<IncomeCard> from db package) ***
                incomeCardAdapter.updateData(displayCards);
            }
        });

        // Observe the actual cards (from database) for pie chart and total calculation
        // *** Ensure IncomeViewModel has getActualCardsLiveData() method returning LiveData<List<IncomeCard>> ***
        incomeViewModel.getActualCardsLiveData().observe(getViewLifecycleOwner(), actualCards -> {
            Log.d(TAG, "Observer: Actual income cards from DB updated, count: " + (actualCards != null ? actualCards.size() : "null"));
            if (actualCards != null) { // No need to check for null list if LiveData emits it, but good practice for the content
                loadIncomePieChartData(actualCards);
                updateTotalIncomeUI(actualCards);
            }
        });

        return view;
    }

    private void setupIncomePieChart() {
        if (incomePieChart == null) {
            Log.e(TAG, "setupIncomePieChart: incomePieChart is null!");
            return;
        }
        incomePieChart.setUsePercentValues(true);
        incomePieChart.setDrawHoleEnabled(true);
        incomePieChart.setHoleColor(Color.parseColor("#FFFFFF"));
        incomePieChart.setTransparentCircleColor(Color.parseColor("#FAFAFA"));
        incomePieChart.setTransparentCircleAlpha(100);
        incomePieChart.setCenterTextSize(16f);
        incomePieChart.setDrawCenterText(true);
        incomePieChart.setHighlightPerTapEnabled(true);
        incomePieChart.getDescription().setEnabled(false);
        incomePieChart.getLegend().setEnabled(false);
        incomePieChart.setEntryLabelColor(Color.DKGRAY);
        incomePieChart.setEntryLabelTextSize(10f);
        incomePieChart.setRotationEnabled(false);
        incomePieChart.setNoDataText("No income data yet.");
    }

    private void loadIncomePieChartData(List<IncomeCard> actualIncomeCards) {
        if (incomePieChart == null) {
            Log.e(TAG, "loadIncomePieChartData: PieChart is null.");
            return;
        }
        Log.d(TAG, "loadIncomePieChartData. Actual income cards: " + actualIncomeCards.size());

        if (actualIncomeCards.isEmpty()) { // No need to check for null if LiveData guarantees non-null list
            incomePieChart.clear();
            incomePieChart.invalidate(); // Shows "No data text" set in setup
            return;
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        ArrayList<Integer> sliceColors = new ArrayList<>();

        for (IncomeCard card : actualIncomeCards) {
            // card here is com.example.blushfinance.db.IncomeCard
            entries.add(new PieEntry(card.getAmount(), card.getName()));
            sliceColors.add(card.getItemColor());
        }

        PieDataSet dataSet = new PieDataSet(entries, "Income Categories");
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
            data.setValueFormatter(new CustomValueFormatter(incomePieChart));
        }
        data.setValueTextColor(Color.DKGRAY);

        incomePieChart.setData(data);
        incomePieChart.invalidate();
        incomePieChart.animateY(1000, Easing.EaseInOutQuad);
    }

    private void updateTotalIncomeUI(List<IncomeCard> actualIncomeCards) {
        float total = 0;
        for (IncomeCard card : actualIncomeCards) { // card here is com.example.blushfinance.db.IncomeCard
            total += card.getAmount();
        }
        String totalText = String.format(Locale.UK, "Total Income: £%.2f", total);
        if (incomePieChart != null) {
            incomePieChart.setCenterText(totalText);
        }
    }

    private int getNextIncomeColor() {
        if (incomeColorPalette == null || incomeColorPalette.isEmpty()) {
            return Color.GRAY;
        }
        // Get current actual cards to determine next color index to avoid immediate reuse
        // if items are deleted and then added.
        List<IncomeCard> currentActualCards = incomeViewModel.getActualCardsLiveData().getValue();
        int nextIndex = (currentActualCards != null ? currentActualCards.size() : 0) % incomeColorPalette.size();
        return incomeColorPalette.get(nextIndex);
    }

    @Override
    public void onPotAdded(String name, float amount) {
        Log.d(TAG, "New income received from dialog: " + name + ", Amount: " + amount);

        int color = getNextIncomeColor();
        // *** Ensure IncomeCard (from db package) constructor is (String, float, int) ***
        IncomeCard newIncomeCard = new IncomeCard(name, amount, color);

        // *** Ensure IncomeViewModel has addIncomeCard(IncomeCard from db package) method ***
        incomeViewModel.addIncomeCard(newIncomeCard);
        // LiveData observers will handle UI updates.
    }

    public void showAddPotDialog() {
        // *** Ensure AddPotDialogFragment exists, defines AddPotDialogListener, and has newInstance(listener) ***
        AddPotDialogFragment dialog = AddPotDialogFragment.newInstance(this);
        dialog.show(getParentFragmentManager(), "AddIncomeDialogFragmentTag");
    }
}
