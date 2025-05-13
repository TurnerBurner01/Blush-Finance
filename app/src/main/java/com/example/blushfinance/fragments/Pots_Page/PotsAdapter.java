package com.example.blushfinance.fragments.Pots_Page;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.blushfinance.R;

import java.util.List;
import java.util.Locale;

public class PotsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Pot> pots;
    private Context context;
    private static final int ITEM_POT = 0;
    private static final int ITEM_PLUS = 1;
    private PotsFragment potsFragment;

    public PotsAdapter(List<Pot> pots, Context context, PotsFragment potsFragment) {
        this.pots = pots;
        this.context = context;
        this.potsFragment = potsFragment;
    }

    @Override
    public int getItemViewType(int position) {
        return pots.get(position) == null ? ITEM_PLUS : ITEM_POT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_POT) {
            View view = LayoutInflater.from(context).inflate(R.layout.p_item_pot, parent, false);
            return new PotViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.p_item_pot_plus, parent, false);
            return new PlusViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PotViewHolder) {
            Pot pot = pots.get(position);
            ((PotViewHolder) holder).bindPotData(pot);
        } else if (holder instanceof PlusViewHolder) {
            ((PlusViewHolder) holder).bindPlusIcon();
        }
    }

    @Override
    public int getItemCount() {
        return pots.size();
    }

    // ViewHolder for regular pots
    static class PotViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView, incomeText;
        private ImageView iconView;
        private View background;

        public PotViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.pot_name);
            incomeText = itemView.findViewById(R.id.incomeText);
            iconView = itemView.findViewById(R.id.pot_icon);
            background = itemView.findViewById(R.id.pot_background);
        }

        public void bindPotData(Pot pot) {
            nameTextView.setText(pot.getName());
            incomeText.setText(String.format(Locale.UK, "£%,.2f", (float) pot.getMaxAmount())); // Display max amount
            iconView.setImageResource(pot.getIconResId());

            // Keeps the background round
            Drawable drawable = background.getBackground();
            if (drawable instanceof GradientDrawable) {
                GradientDrawable gradientDrawable = (GradientDrawable) drawable.mutate();
                int color = ContextCompat.getColor(itemView.getContext(), pot.getColorResId());
                gradientDrawable.setColor(color);
            }
        }
    }

    // ViewHolder for the plus icon
    class PlusViewHolder extends RecyclerView.ViewHolder {
        private TextView plusTextView;

        public PlusViewHolder(View itemView) {
            super(itemView);
            plusTextView = itemView.findViewById(R.id.plus_text);
        }

        public void bindPlusIcon() {
            plusTextView.setOnClickListener(v -> showAddPotDialog());
        }
    }

    // Open the dialog to add a new pot
    private void showAddPotDialog() {
        AddPotDialogFragment dialog = new AddPotDialogFragment(potsFragment);
        dialog.setPotAddedListener(new AddPotDialogFragment.PotAddedListener() {
            @Override
            public void onPotAdded(Pot newPot) {
                // Check if there is enough available income to add the pot
                if (newPot.getMaxAmount() <= potsFragment.getTotalIncome()) {
                    // Proceed to add the pot and update the income
                    potsFragment.addNewPot(newPot);
                    potsFragment.deductIncome(newPot.getMaxAmount());
                } else {
                    // Show a message or toast that there isn't enough income
                    Toast.makeText(context, "Not enough income to add this pot!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "AddPotDialog");
    }
}

