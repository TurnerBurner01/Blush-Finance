package com.example.blushfinance.fragments.Pots_Page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blushfinance.R;

import java.util.List;

public class PotsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Pot> pots;
    private Context context;
    private static final int ITEM_POT = 0;
    private static final int ITEM_PLUS = 1;
    private PotsFragment potsFragment;

    // Constructor for the adapter
    public PotsAdapter(List<Pot> pots, Context context, PotsFragment potsFragment) {
        this.pots = pots;
        this.context = context;
        this.potsFragment = potsFragment;
    }

    @Override
    public int getItemViewType(int position) {
        // Check if this item is a plus button
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
        private TextView nameTextView;

        public PotViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.pot_name);
        }

        // Bind pot data to the views
        public void bindPotData(Pot pot) {
            nameTextView.setText(pot.getName());
        }
    }

    // ViewHolder for the plus icon
    class PlusViewHolder extends RecyclerView.ViewHolder {
        private TextView plusTextView;

        public PlusViewHolder(View itemView) {
            super(itemView);
            plusTextView = itemView.findViewById(R.id.plus_text);
        }

        // Handle the click on the plus icon
        public void bindPlusIcon() {
            plusTextView.setOnClickListener(v -> {
                // Shows the dialog
                showAddPotDialog();
            });
        }
    }

    // Open the dialog to add a new pot
    private void showAddPotDialog() {
        AddPotDialogFragment dialog = new AddPotDialogFragment(potsFragment);
        dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "AddPotDialog");
    }
}
