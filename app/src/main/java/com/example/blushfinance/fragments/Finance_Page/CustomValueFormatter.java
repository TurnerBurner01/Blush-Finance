package com.example.blushfinance.fragments.Finance_Page;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;

import java.text.DecimalFormat; // Not strictly needed if using String.format for everything
import java.util.ArrayList;
import java.util.List;
import java.util.Locale; // For Locale specific formatting

public class CustomValueFormatter extends ValueFormatter {

    // mFormat is not strictly necessary if we use String.format for all outputs.
    // private final DecimalFormat mFormat;
    private PieChart pieChart;

    public CustomValueFormatter(PieChart pieChart) {
        // this.mFormat = new DecimalFormat("###,##0.00", new java.text.DecimalFormatSymbols(Locale.UK)); // For two decimal places
        this.pieChart = pieChart;
    }

    @Override
    public String getFormattedValue(float percentValue) {
        if (pieChart == null || pieChart.getData() == null || pieChart.getData().getDataSet() == null) { // Check getDataSet() too
            return String.format(Locale.UK, "£0.00 (%.1f%%)", percentValue);
        }

        IPieDataSet dataSet = pieChart.getData().getDataSet();
        if (dataSet.getEntryCount() == 0) {
            return String.format(Locale.UK, "£0.00 (%.1f%%)", percentValue);
        }

        float totalSum = 0f;
        List<PieEntry> currentEntries = new ArrayList<>();
        for (int i = 0; i < dataSet.getEntryCount(); i++) {
            PieEntry entry = dataSet.getEntryForIndex(i);
            if (entry != null) {
                currentEntries.add(entry);
                totalSum += entry.getY();
            }
        }

        if (totalSum == 0) {
            return String.format(Locale.UK, "£0.00 (%.1f%%)", percentValue);
        }

        for (PieEntry entry : currentEntries) {
            float entryCalculatedPercent = (entry.getY() / totalSum) * 100f;
            if (Math.abs(entryCalculatedPercent - percentValue) < 0.01f) { // Reduced tolerance slightly, can be sensitive
                return String.format(Locale.UK, "%s: £%.2f (%.1f%%)",
                        entry.getLabel(),
                        entry.getY(),
                        percentValue); // percentValue is already calculated by the library for the slice
            }
        }

        // Fallback: This should ideally not be hit if the loop logic is robust.
        // If it is hit, it means the percentValue from the library didn't exactly match
        // any of our manually calculated percentages.
        float approximateActualValue = (percentValue / 100f) * totalSum;
        String labelForApprox = "Value"; // Generic label if specific one can't be determined reliably here
        if (!currentEntries.isEmpty() && currentEntries.size() == 1 && Math.abs(percentValue - 100f) < 0.1f) {
            // If it's 100% and only one entry, use its label for the fallback
            labelForApprox = currentEntries.get(0).getLabel();
        }

        return String.format(Locale.UK, "%s (approx): £%.2f (%.1f%%)",
                labelForApprox,
                approximateActualValue,
                percentValue);
    }
}