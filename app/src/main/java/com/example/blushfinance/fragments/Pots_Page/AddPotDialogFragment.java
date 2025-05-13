package com.example.blushfinance.fragments.Pots_Page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import com.example.blushfinance.R;

public class AddPotDialogFragment extends DialogFragment {

    private EditText nameInput, maxAmountInput;
    private Spinner typeSpinner;
    private PotsFragment potsFragment;

    public AddPotDialogFragment(PotsFragment potsFragment) {
        this.potsFragment = potsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.p_dialog_add_pot, container, false);

        nameInput = view.findViewById(R.id.name_input);
        maxAmountInput = view.findViewById(R.id.max_amount_input);
        typeSpinner = view.findViewById(R.id.type_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(), R.array.pot_types, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String maxAmountStr = maxAmountInput.getText().toString().trim();
            String type = typeSpinner.getSelectedItem().toString();

            if (name.isEmpty() || maxAmountStr.isEmpty()) {
                Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int maxAmount = Integer.parseInt(maxAmountStr);
                Pot newPot = PotFactory.createPot(type, name, maxAmount);

                if (potsFragment != null) {
                    potsFragment.addNewPot(newPot);
                }

                dismiss();
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Max amount must be a number", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
