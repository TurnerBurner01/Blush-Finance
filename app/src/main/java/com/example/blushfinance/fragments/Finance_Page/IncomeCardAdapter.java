package com.example.blushfinance.fragments.Finance_Page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.blushfinance.R;
import java.util.List;

public class IncomeCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<IncomeCard> incomeCards;
    private Context context;
    private static final int INCOME_CARD = 0;
    private static final int ADD_INCOME_CARD = 1;
    private IncomeFragment incomeFragment;

    public IncomeCardAdapter(List<IncomeCard> incomeCards, Context context, IncomeFragment incomeFragment) {
        this.incomeCards = incomeCards;
        this.context = context;
        this.incomeFragment = incomeFragment;
    }

    @Override
    public int getItemViewType(int position) {
        // Check if the current item is the "plus" card (represented by null)
        if (incomeCards.get(position) == null) {
            return ADD_INCOME_CARD;
        } else {
            return INCOME_CARD;
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == INCOME_CARD) {
            View view = inflater.inflate(R.layout.income_pot, parent, false);
            return new PotViewHolder(view);
        } else if (viewType == ADD_INCOME_CARD) {
            View view = inflater.inflate(R.layout.new_income_pot, parent, false);
            return new PlusViewHolder(view, incomeFragment); // Pass incomeFragment
        } else {
            throw new IllegalArgumentException("Invalid viewType: " + viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PotViewHolder) {
            IncomeCard incomeCard = incomeCards.get(position);
            ((PotViewHolder) holder).bindPotData(incomeCard);
        } else if (holder instanceof PlusViewHolder) {
            ((PlusViewHolder) holder).bindPlusIcon();
        }
    }



    @Override
    public int getItemCount() {
        return incomeCards.size(); // +1 for the add income button
    }

    // ViewHolder for regular income cards
    class PotViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView, incomeText;

        public PotViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.incomeCardTitle);
            incomeText = itemView.findViewById(R.id.incomeCardAmount);
        }

        public void bindPotData(IncomeCard incomeCard) {
            nameTextView.setText(incomeCard.getName());
            incomeText.setText("£" + incomeCard.getAmount());
        }

    }

    // ViewHolder for the plus income card
    class PlusViewHolder extends RecyclerView.ViewHolder {
        private TextView plusTextView;
        private IncomeFragment fragment;

        public PlusViewHolder(View itemView, IncomeFragment fragment) {
            super(itemView);
            this.fragment = fragment;
            plusTextView = itemView.findViewById(R.id.newIncomeCardText);

            plusTextView.setOnClickListener(v -> {
                if (fragment != null) {
                    fragment.showAddPotDialog();
                }
            });
        }

        public void bindPlusIcon() {
            // No additional logic needed for now
        }
    }

}