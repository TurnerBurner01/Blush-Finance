package com.example.blushfinance.fragments.Finance_Page; // Or wherever FinanceFragment is

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button; // Example for switching

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.blushfinance.R;

public class FinanceFragment extends Fragment {

    private Button switchToIncomeButton; // Example
    private Button switchToExpensesButton; // Example
    private boolean isShowingIncome = true; // To track current state

    public FinanceFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finance, container, false); // Ensure you have this layout

        // Example: Find buttons if you have them in fragment_finance.xml
        // switchToIncomeButton = view.findViewById(R.id.button_to_income);
        // switchToExpensesButton = view.findViewById(R.id.button_to_expenses);

        // If you have a single button to toggle, like in your screenshots:
        Button switchButton = view.findViewById(R.id.switch_finance_mode_button); // ** YOU NEED THIS BUTTON IN fragment_finance.xml **
        if (switchButton != null) {
            updateSwitchButtonText(switchButton); // Set initial text
            switchButton.setOnClickListener(v -> {
                if (isShowingIncome) {
                    loadFragment(new ExpensesFragment(), "EXPENSES_TAG");
                    isShowingIncome = false;
                } else {
                    loadFragment(new IncomeFragment(), "INCOME_TAG");
                    isShowingIncome = true;
                }
                updateSwitchButtonText(switchButton);
            });
        }


        // Load the initial fragment (e.g., IncomeFragment)
        if (savedInstanceState == null) { // Important to avoid adding fragment on config change
            loadFragment(new IncomeFragment(), "INCOME_TAG");
            isShowingIncome = true;
            if (switchButton != null) updateSwitchButtonText(switchButton);
        } else {
            // Restore state if needed, e.g., which fragment was visible
            // isShowingIncome = savedInstanceState.getBoolean("isShowingIncomeState", true);
            // FragmentManager usually handles restoring the last fragment in the container if using replace
        }

        return view;
    }

    private void updateSwitchButtonText(Button button) {
        if (button == null) return;
        if (isShowingIncome) {
            button.setText("Expenses"); // Button shows what you'll switch TO
        } else {
            button.setText("Income");
        }
    }

    private void loadFragment(Fragment fragment, String tag) {
        // Use getChildFragmentManager() if FinanceFragment is hosting these as child fragments
        // Use getParentFragmentManager() or requireActivity().getSupportFragmentManager()
        // if Income/Expenses are replacing FinanceFragment or are at the same level in Activity.
        // Assuming Income/Expenses are children of FinanceFragment:
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back.
        // *** YOU NEED A CONTAINER (e.g., FrameLayout) IN fragment_finance.xml WITH ID 'finance_content_container' ***
        transaction.replace(R.id.finance_content_container, fragment, tag);
        // transaction.addToBackStack(null); // Optional: if you want back navigation between income/expense

        // Commit the transaction
        transaction.commit();
    }

    // Optional: to save the state of which fragment is showing
    // @Override
    // public void onSaveInstanceState(@NonNull Bundle outState) {
    //     super.onSaveInstanceState(outState);
    //     outState.putBoolean("isShowingIncomeState", isShowingIncome);
    // }
}
