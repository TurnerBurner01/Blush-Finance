package com.example.blushfinance.fragments.Finance_Page;

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

public class AddPotDialogFragment extends DialogFragment {

    // --- Define the listener interface INSIDE AddPotDialogFragment ---
    // It must be public so IncomeFragment can implement AddPotDialogFragment.AddPotDialogListener
    public interface AddPotDialogListener {
        void onPotAdded(String name, float amount); // Or whatever parameters your IncomeFragment expects
    }
    // --- End of interface definition ---

    private AddPotDialogListener listener;
    private EditText nameEditText; // Make sure this ID matches your dialog_add_income_card.xml
    private EditText amountEditText; // Make sure this ID matches your dialog_add_income_card.xml

    /**
     * Static factory method to create a new instance of the dialog,
     * passing the listener. This is a recommended pattern for DialogFragments.
     * @param listener The listener that will receive the callback.
     * @return A new instance of AddPotDialogFragment.
     */
    public static AddPotDialogFragment newInstance(AddPotDialogListener listener) {
        AddPotDialogFragment fragment = new AddPotDialogFragment();
        fragment.listener = listener;
        return fragment;
    }

    /**
     * Default constructor. Required for DialogFragment recreation by the system.
     */
    public AddPotDialogFragment() {
        // Default constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Ensure the listener is set. This is a robust way to get the listener
        // if it wasn't passed via newInstance or if the fragment is recreated.
        if (listener == null) { // Only if not already set by newInstance
            if (getParentFragment() instanceof AddPotDialogListener) {
                listener = (AddPotDialogListener) getParentFragment();
            } else if (context instanceof AddPotDialogListener) {
                // This would mean the Activity implements the listener,
                // which is less common for fragment-to-fragment communication.
                listener = (AddPotDialogListener) context;
            }
        }
        // If the listener is still null after trying these, it's an error in how the dialog was shown or implemented.
        if (listener == null) {
            throw new ClassCastException(
                    (getParentFragment() != null ? getParentFragment().toString() : context.toString()) +
                            " must implement AddPotDialogListener"
            );
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Ensure you have 'dialog_add_income_card.xml' in your res/layout folder
        View view = inflater.inflate(R.layout.dialog_add_income_card, null);

        // *** Replace R.id.* with the actual IDs from your dialog_add_income_card.xml ***
        nameEditText = view.findViewById(R.id.name_input);     // Example ID, ensure it matches your XML
        amountEditText = view.findViewById(R.id.amount_input); // Example ID, ensure it matches your XML
        Button addButton = view.findViewById(R.id.add_button);       // Example ID, ensure it matches your XML
        // Button cancelButton = view.findViewById(R.id.cancel_button); // If you have a cancel button

        builder.setView(view)
                .setTitle("Add New Income Pot"); // Or your desired dialog title

        final AlertDialog dialog = builder.create(); // Create dialog to reference it in listener

        addButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim();
            String amountStr = amountEditText.getText().toString().trim();

            boolean isValid = true;
            if (TextUtils.isEmpty(name)) {
                nameEditText.setError("Name cannot be empty.");
                isValid = false;
            }
            if (TextUtils.isEmpty(amountStr)) {
                amountEditText.setError("Amount cannot be empty.");
                isValid = false;
            }

            if (!isValid) {
                return; // Stop processing if validation fails
            }

            try {
                float amount = Float.parseFloat(amountStr);
                if (amount <= 0) {
                    amountEditText.setError("Amount must be positive.");
                    return; // Stop if amount is not positive
                }

                // If input is valid and listener is set, call the listener's method
                if (listener != null) {
                    listener.onPotAdded(name, amount);
                    dismiss(); // Close the dialog after successful addition
                } else {
                    // This should ideally not be reached if onAttach is correctly implemented
                    Toast.makeText(getContext(), "Error: Listener not configured.", Toast.LENGTH_SHORT).show();
                }

            } catch (NumberFormatException e) {
                // Handle cases where the amount entered is not a valid number
                amountEditText.setError("Invalid amount format.");
            }
        });

        // if (cancelButton != null) {
        //     cancelButton.setOnClickListener(v -> dismiss());
        // }

        return dialog;
    }
}
