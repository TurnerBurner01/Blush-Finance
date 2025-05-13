package com.example.blushfinance.fragments.Pots_Page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.blushfinance.R;
import com.example.blushfinance.db.IncomeCard;
import com.example.blushfinance.fragments.Finance_Page.IncomeViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PotsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PotsAdapter potsAdapter;
    private List<Pot> potList;
    private IncomeViewModel incomeVM;
    private float totalIncome = 0f;
    private TextView incomeTextView;

    public PotsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.p_activity_pots, container, false);

        // Adjust window insets to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.pots), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2)); // Grid layout with 2 columns

        potList = new ArrayList<>();  // Initialize pot list

        potsAdapter = new PotsAdapter(potList, getContext(), this);
        recyclerView.setAdapter(potsAdapter);

        // Add the plus icon item at the end of the list
        potList.add(null);  // Add a placeholder for the plus item

        // Set up income text view
        incomeTextView = view.findViewById(R.id.incomeText);

        // Set up ViewModel
        incomeVM = new ViewModelProvider(requireActivity()).get(IncomeViewModel.class);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        incomeVM.getActualCardsLiveData().observe(getViewLifecycleOwner(), list -> {
            float sum = 0f;
            if (list != null) {
                for (IncomeCard card : list) {
                    sum += card.getAmount();
                }
            }
            totalIncome = sum;
            updateIncomeUi();
        });
    }

    private void updateIncomeUi() {
        incomeTextView.setText(String.format(Locale.UK, "£%.2f", totalIncome));
    }

    public void addNewPot(Pot newPot) {
        if (!potList.isEmpty() && potList.get(potList.size() - 1) == null) {
            potList.remove(potList.size() - 1);  // Remove the plus pot
        }

        potList.add(newPot);
        potList.add(null);  // 'null' represents the plus icon
        potsAdapter.notifyDataSetChanged();
    }
}
