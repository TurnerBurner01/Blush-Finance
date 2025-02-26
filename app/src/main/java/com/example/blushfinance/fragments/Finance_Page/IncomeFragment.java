package com.example.blushfinance.fragments.Finance_Page;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log; // Import for Log
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.blushfinance.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class IncomeFragment extends Fragment {

    private PieChart pieChart;
    private RecyclerView recyclerView;
    private IncomeCardAdapter incomeCardAdapter;
    private List<IncomeCard> incomeCards;

    public IncomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        // Initialize PieChart
        pieChart = view.findViewById(R.id.pie_chart);
        setupPieChart();
        loadPieChartData();

        // Initialize RecyclerView and adapter
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Initialize list of income cards objects.  **CRITICAL: Add the initial 'null' for the plus button**
        incomeCards = new ArrayList<>();
        incomeCards.add(null); // Start with the plus card

        incomeCardAdapter = new IncomeCardAdapter(incomeCards, getContext(), this);
        recyclerView.setAdapter(incomeCardAdapter);

        Log.d("IncomeFragment", "onCreateView: Adapter set, initial card count: " + incomeCardAdapter.getItemCount()); // Debugging

        return view;
    }

    private void setupPieChart() {
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setCenterText("Income Distribution");
        pieChart.setCenterTextSize(10f);
        pieChart.setDrawCenterText(true);

        // REMOVE the description label
        pieChart.getDescription().setEnabled(false);

        // REMOVE the legend
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();

        // Example data - replace with your actual data
        entries.add(new PieEntry(40f, "Salary"));
        entries.add(new PieEntry(30f, "Freelance"));
        entries.add(new PieEntry(20f, "Investments"));
        entries.add(new PieEntry(10f, "Others"));

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW);  // Customize colors
        PieData data = new PieData(dataSet);
        pieChart.setData(data);
        pieChart.invalidate(); // Refresh the chart
    }
    public void addNewIncomeCard(IncomeCard incomeCard) {
        // Find and remove the existing 'null' (plus card)
        if (!incomeCards.isEmpty() && incomeCards.contains(null)) {
            incomeCards.remove(null);
        }

        // Add the new income card
        incomeCards.add(incomeCard);

        // Add a new 'null' to represent the plus button at the end
        incomeCards.add(null);

        // Notify the adapter
        incomeCardAdapter.notifyDataSetChanged();
        Log.d("IncomeFragment", "addNewIncomeCard: Card added, new count: " + incomeCardAdapter.getItemCount()); // Debugging

    }



    public void showAddPotDialog() {
        AddPotDialogFragment dialog = new AddPotDialogFragment(this);
        dialog.show(getParentFragmentManager(), "AddPotDialogFragment");
    }

}