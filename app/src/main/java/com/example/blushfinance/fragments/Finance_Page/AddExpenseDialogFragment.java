package com.example.blushfinance.fragments.Finance_Page; // Or your dialogs package

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.blushfinance.R; // Your R file

/**
 * DialogFragment for adding a new expense item.
 * Uses a listener interface to communicate the result back to the calling fragment.
 */
public class AddExpenseDialogFragment extends DialogFragment {

    /**
     * Interface definition for a callback to be invoked when an expense is added.
     */
    public interface AddExpenseDialogListener {
        /**
         * Called when the user confirms adding a new expense.
         * @param name The name/category of the expense entered by the user.
         * @param amount The amount of the expense entered by the user.
         */
        void onExpenseAdded(String name, float amount);
    }

    private AddExpenseDialogListener listener;
    private EditText nameEditText;
    private EditText amountEditText;

    /**
     * Creates a new instance of AddExpenseDialogFragment.
     * @param listener The listener that will receive the callback when an expense is added.
     * @return A new instance of AddExpenseDialogFragment.
     */
    public static AddExpenseDialogFragment newInstance(AddExpenseDialogListener listener) {
        AddExpenseDialogFragment fragment = new AddExpenseDialogFragment();
        fragment.listener = listener; // Set the listener
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure the listener is set. If not passed via newInstance (e.g., due to process death
        // and recreation), try to get it from the parent fragment or activity.
        if (listener == null) {
            if (getParentFragment() instanceof AddExpenseDialogListener) {
                listener = (AddExpenseDialogListener) getParentFragment();
            } else if (context instanceof AddExpenseDialogListener) {
                listener = (AddExpenseDialogListener) context;
            }
        }
        // If the listener is still null after trying these, throw an exception.
        if (listener == null) {
            throw new ClassCastException("Hosting Fragment/Activity must implement AddExpenseDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Inflate the custom layout for the dialog.
        // *** YOU NEED TO CREATE 'dialog_add_expense_card.xml' ***
        View view = inflater.inflate(R.layout.dialog_add_expense_card, null);

        // Find the views within the custom layout.
        // *** Replace R.id.* with the actual IDs from your XML layout ***
        nameEditText = view.findViewById(R.id.editTextExpenseName);     // Example ID
        amountEditText = view.findViewById(R.id.editTextExpenseAmount); // Example ID
        Button addButton = view.findViewById(R.id.buttonAddExpense);       // Example ID
        Button cancelButton = view.findViewById(R.id.buttonCancelExpense); // Example ID

        builder.setView(view)
                .setTitle("Add New Expense"); // Set the dialog title

        AlertDialog dialog = builder.create();

        // Set the click listener for the "Add" button
        addButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String amountStr = amountEditText.getText().toString().trim();

            // --- Input Validation ---
            boolean valid = true;
            if (TextUtils.isEmpty(name)) {
                nameEditText.setError("Expense name cannot be empty.");
                valid = false;
            }
            if (TextUtils.isEmpty(amountStr)) {
                amountEditText.setError("Amount cannot be empty.");
                valid = false;
            }

            if (!valid) {
                return; // Stop if validation fails
            }

            // --- Process Valid Input ---
            try {
                float amount = Float.parseFloat(amountStr);
                // Ensure amount is positive
                if (amount <= 0) {
                    amountEditText.setError("Amount must be positive.");
                    return; // Stop if amount is not positive
                }

                // If input is valid and listener is set, call the listener's method
                if (listener != null) {
                    listener.onExpenseAdded(name, amount);
                    dismiss(); // Close the dialog after successful addition
                } else {
                    // Should not happen if onAttach logic is correct, but good to handle
                    Toast.makeText(getContext(), "Error: Listener not available.", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                // Handle cases where the amount entered is not a valid number
                amountEditText.setError("Invalid amount format.");
            }
        });

        // Set the click listener for the "Cancel" button
        cancelButton.setOnClickListener(v -> dismiss()); // Simply close the dialog

        return dialog;
    }
}
