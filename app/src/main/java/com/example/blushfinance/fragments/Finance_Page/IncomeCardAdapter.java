package com.example.blushfinance.fragments.Finance_Page;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blushfinance.R; // Your R file
import com.example.blushfinance.db.IncomeCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class IncomeCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_INCOME_POT = 1;
    private static final int VIEW_TYPE_ADD_NEW = 2;

    private List<IncomeCard> incomeCardsList;
    private Context context;
    private IncomeFragment fragment; // *** Ensure this is correctly typed ***

    // *** Constructor should accept IncomeFragment ***
    public IncomeCardAdapter(List<IncomeCard> incomeCardsList, Context context, IncomeFragment fragment) {
        this.incomeCardsList = new ArrayList<>(incomeCardsList);
        this.context = context;
        this.fragment = fragment; // Assign the passed IncomeFragment instance
    }

    public void updateData(List<IncomeCard> newCards) {
        this.incomeCardsList.clear();
        if (newCards != null) {
            this.incomeCardsList.addAll(newCards);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (incomeCardsList.get(position) == null) {
            return VIEW_TYPE_ADD_NEW;
        } else {
            return VIEW_TYPE_INCOME_POT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_INCOME_POT) {
            View view = inflater.inflate(R.layout.income_pot, parent, false);
            return new IncomePotViewHolder(view);
        } else { // VIEW_TYPE_ADD_NEW
            View view = inflater.inflate(R.layout.new_income_pot, parent, false);
            return new AddNewViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_INCOME_POT) {
            ((IncomePotViewHolder) holder).bindData(incomeCardsList.get(position));
        } else {
            ((AddNewViewHolder) holder).itemView.setOnClickListener(v -> {
                if (fragment != null) {
                    // *** This calls a method on the IncomeFragment instance ***
                    fragment.showAddPotDialog();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return incomeCardsList != null ? incomeCardsList.size() : 0;
    }

    static class IncomePotViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, amountTextView;
        CardView cardViewToColor;

        public IncomePotViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.incomeCardTitle);
            amountTextView = itemView.findViewById(R.id.incomeCardAmount);
            // Assuming the root of income_pot.xml is the CardView you want to color
            // Or find it by ID if it's nested, e.g., itemView.findViewById(R.id.your_card_view_id)
            // Based on your previous XML for expenses, it was R.id.audioCard. Adjust for income_pot.xml
            cardViewToColor = itemView.findViewById(R.id.audioCard); // Example ID, ensure it's correct for income_pot.xml
        }

        void bindData(IncomeCard incomeCard) {
            if (incomeCard == null) return;

            nameTextView.setText(incomeCard.getName());
            amountTextView.setText(String.format(Locale.UK, "£%.2f", incomeCard.getAmount()));

            if (cardViewToColor != null) {
                // *** This line needs incomeCard.getItemColor() ***
                cardViewToColor.setCardBackgroundColor(incomeCard.getItemColor());
            }

            int textColor = isColorDark(incomeCard.getItemColor()) ? Color.WHITE : Color.BLACK;
            nameTextView.setTextColor(textColor);
            amountTextView.setTextColor(textColor);
        }

        private boolean isColorDark(int color) {
            double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
            return darkness >= 0.5;
        }
    }

    static class AddNewViewHolder extends RecyclerView.ViewHolder {
        public AddNewViewHolder(View itemView) {
            super(itemView);
        }
    }
}
