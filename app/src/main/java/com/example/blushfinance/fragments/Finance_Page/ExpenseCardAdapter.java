package com.example.blushfinance.fragments.Finance_Page; // Or your adapters package

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView; // Import CardView
import androidx.recyclerview.widget.RecyclerView;

import com.example.blushfinance.R; // Your R file
import com.example.blushfinance.db.ExpenseCard;
// Adjust import for ExpenseCard model location
// import com.example.blushfinance.models.ExpenseCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView Adapter for displaying expense cards and an "add" button.
 * Updated ViewHolder to match the provided XML layout.
 */
public class ExpenseCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // View types to distinguish between regular expense items and the add button
    private static final int VIEW_TYPE_EXPENSE_POT = 1;
    private static final int VIEW_TYPE_ADD_NEW = 2;

    private List<ExpenseCard> expenseCardsList; // This list includes 'null' for the add button
    private Context context;
    private ExpensesFragment fragment; // Reference to the hosting fragment to trigger dialog

    /**
     * Constructor for the adapter.
     * @param expenseCardsList The initial list of items (including null for add button).
     * @param context The application context.
     * @param fragment The hosting ExpensesFragment instance (for callbacks).
     */
    public ExpenseCardAdapter(List<ExpenseCard> expenseCardsList, Context context, ExpensesFragment fragment) {
        // Create a mutable copy of the list
        this.expenseCardsList = new ArrayList<>(expenseCardsList);
        this.context = context;
        this.fragment = fragment;
    }

    /**
     * Updates the data displayed by the adapter.
     * @param newCards The new list of items (including null for add button).
     */
    public void updateData(List<ExpenseCard> newCards) {
        this.expenseCardsList.clear();
        if (newCards != null) {
            this.expenseCardsList.addAll(newCards);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (expenseCardsList.get(position) == null) {
            return VIEW_TYPE_ADD_NEW;
        } else {
            return VIEW_TYPE_EXPENSE_POT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_EXPENSE_POT) {
            // Inflate the layout file you provided (assuming it's named expense_pot.xml)
            View view = inflater.inflate(R.layout.expense_pot, parent, false);
            return new ExpensePotViewHolder(view);
        } else { // VIEW_TYPE_ADD_NEW
            // Inflate the layout for the "add new" button/card.
            // *** YOU NEED TO CREATE 'new_expense_pot.xml' layout file (or similar) ***
            View view = inflater.inflate(R.layout.new_expense_pot, parent, false);
            return new AddNewViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_EXPENSE_POT) {
            ((ExpensePotViewHolder) holder).bindData(expenseCardsList.get(position));
        } else {
            ((AddNewViewHolder) holder).itemView.setOnClickListener(v -> {
                if (fragment != null) {
                    fragment.showAddExpenseDialog();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return expenseCardsList != null ? expenseCardsList.size() : 0;
    }

    // --- ViewHolders ---

    /**
     * ViewHolder for displaying individual expense card items.
     * Updated constructor to match the provided XML layout.
     */
    static class ExpensePotViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, amountTextView;
        CardView cardViewToColor; // Renamed for clarity, this is the CardView to color

        public ExpensePotViewHolder(View itemView) {
            super(itemView);
            // Find views within the expense_pot.xml layout using the IDs from your XML.
            // Note: Using incomeCardTitle/Amount IDs as per your XML. Change if XML IDs change.
            nameTextView = itemView.findViewById(R.id.incomeCardTitle);
            amountTextView = itemView.findViewById(R.id.incomeCardAmount);
            // Find the CardView using its specific ID from your XML
            cardViewToColor = itemView.findViewById(R.id.audioCard);
        }

        /**
         * Binds ExpenseCard data to the views in the ViewHolder.
         * @param expenseCard The ExpenseCard data object for this item.
         */
        void bindData(ExpenseCard expenseCard) {
            if (expenseCard == null) return; // Safety check

            nameTextView.setText(expenseCard.getName());
            // Format amount as currency
            amountTextView.setText(String.format(Locale.UK, "£%.2f", expenseCard.getAmount()));

            // Set the background color of the specific CardView found by ID
            if (cardViewToColor != null) { // Check if CardView was found
                cardViewToColor.setCardBackgroundColor(expenseCard.getItemColor());
            }

            // Adjust text color based on background brightness for better contrast
            // Note: This adjusts text color based on the card background color.
            int textColor = isColorDark(expenseCard.getItemColor()) ? Color.WHITE : Color.BLACK;
            nameTextView.setTextColor(textColor);
            amountTextView.setTextColor(textColor); // Assuming amount text should also contrast
        }

        /**
         * Helper method to determine if a color is dark or light.
         * Used to set contrasting text color.
         * @param color The color integer.
         * @return true if the color is considered dark, false otherwise.
         */
        private boolean isColorDark(int color) {
            // Calculate perceived luminance (YIQ formula)
            double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
            // Consider the color dark if luminance is below a threshold (e.g., 0.5)
            return darkness >= 0.5;
        }
    }

    /**
     * ViewHolder for the "Add New" button/card.
     */
    static class AddNewViewHolder extends RecyclerView.ViewHolder {
        // You might have an ImageView or TextView inside this ViewHolder's layout
        // Example: ImageView addIcon;

        public AddNewViewHolder(View itemView) {
            super(itemView);
            // Find views within the new_expense_pot.xml layout if needed
            // Example: addIcon = itemView.findViewById(R.id.add_expense_icon);
        }
    }
}
