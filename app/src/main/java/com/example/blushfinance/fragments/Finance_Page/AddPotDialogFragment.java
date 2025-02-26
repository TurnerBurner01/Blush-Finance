package com.example.blushfinance.fragments.Finance_Page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.blushfinance.R;

public class AddPotDialogFragment extends DialogFragment {

    private IncomeFragment incomeFragment;
    private EditText nameInput, amountInput;

    // Default constructor (required for DialogFragment)
    public AddPotDialogFragment() {
    }

    // Constructor to pass IncomeFragment
    public AddPotDialogFragment(IncomeFragment incomeFragment) {
        this.incomeFragment = incomeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_income_card, container, false);

        nameInput = view.findViewById(R.id.name_input);
        amountInput = view.findViewById(R.id.amount_input);
        Button addButton = view.findViewById(R.id.add_button);

        addButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String AmountStr = amountInput.getText().toString().trim();

            if (name.isEmpty() || AmountStr.isEmpty()) {
                Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int maxAmount = Integer.parseInt(AmountStr);
                IncomeCard newPot = new IncomeCard(name, maxAmount); // Fix variable name

                if (incomeFragment != null) { // Fix fragment reference
                    incomeFragment.addNewIncomeCard(newPot);
                }

                dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Max amount must be a number", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
