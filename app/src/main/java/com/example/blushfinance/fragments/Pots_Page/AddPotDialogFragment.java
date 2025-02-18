package com.example.blushfinance.fragments.Pots_Page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.fragment.app.DialogFragment;

import com.example.blushfinance.R;

public class AddPotDialogFragment extends DialogFragment {

    private EditText nameInput, colorInput, maxAmountInput;
    private Spinner typeSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.p_dialog_add_pot, container, false);

        nameInput = view.findViewById(R.id.name_input);
        colorInput = view.findViewById(R.id.color_input);
        maxAmountInput = view.findViewById(R.id.max_amount_input);
        typeSpinner = view.findViewById(R.id.type_spinner);

        // Set up the type spinner with pot types
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.pot_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        // Set up the "Add Pot" button
        Button addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString();
            String color = colorInput.getText().toString();
            String maxAmount = maxAmountInput.getText().toString();
            String type = typeSpinner.getSelectedItem().toString();

            Pot newPot = new Pot(name, color, Integer.parseInt(maxAmount), type);
            // Add the new pot to your list and update the RecyclerView
            ((PotsActivity) getActivity()).addNewPot(newPot);
            dismiss();
        });

        return view;
    }
}
