package com.example.blushfinance.fragments.Finance_Page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.example.blushfinance.R;

public class FinanceFragment extends Fragment {

    private Button switchButton;
    private boolean isIncomePage = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance, container, false);

        // Initialize Switch Button
        switchButton = view.findViewById(R.id.btn_switch);

        // Load Income Page by default
        loadFragment(new IncomeFragment());

        // Handle button click to toggle between pages
        switchButton.setOnClickListener(v -> {
            if (isIncomePage) {
                loadFragment(new ExpensesFragment());
                switchButton.setText("Income");
            } else {
                loadFragment(new IncomeFragment());
                switchButton.setText("Expenses");
            }
            isIncomePage = !isIncomePage;
        });

        return view;
    }

    // Method to replace fragment inside FinanceFragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);  // Replacing fragment in FrameLayout
        transaction.commit();
    }
}
