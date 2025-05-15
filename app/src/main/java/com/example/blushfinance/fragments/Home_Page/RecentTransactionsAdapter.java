package com.example.blushfinance.fragments.Home_Page;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blushfinance.R;
import com.example.blushfinance.db.Transaction;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class RecentTransactionsAdapter extends RecyclerView.Adapter<RecentTransactionsAdapter.TxViewHolder> {
    private final List<Transaction> recentList;
    private final NumberFormat currencyFmt;

    public RecentTransactionsAdapter(List<Transaction> recentList) {
        this.recentList = recentList;
        currencyFmt = NumberFormat.getCurrencyInstance(Locale.UK);
        currencyFmt.setMaximumFractionDigits(0);
        currencyFmt.setMinimumFractionDigits(0);
    }

    @NonNull
    @Override
    public TxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_transaction_row, parent, false);
        return new TxViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TxViewHolder holder, int position) {
        Transaction tx = recentList.get(position);

        // Transaction naming
        holder.nameTv.setText(tx.name);

        String formatted = currencyFmt.format(tx.amount);
        if (tx.isIncome) {
            holder.amountTv.setText("+" + formatted);
            // note to self could add txt colour
        } else {
            holder.amountTv.setText("-" + formatted);
            // same colour note
        }
    }

    public int getItemCount() {
        return recentList.size();
    }

    static class TxViewHolder extends RecyclerView.ViewHolder {
        final TextView nameTv;
        final TextView amountTv;

        TxViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.trans_name);
            amountTv = itemView.findViewById(R.id.trans_amount);
        }
    }
}
