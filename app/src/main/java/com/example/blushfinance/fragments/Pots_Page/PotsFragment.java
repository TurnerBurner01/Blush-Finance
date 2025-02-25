package com.example.blushfinance.fragments.Pots_Page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.blushfinance.R;

import java.util.ArrayList;
import java.util.List;

public class PotsFragment extends Fragment {

    private RecyclerView recyclerView;
    private PotsAdapter potsAdapter;
    private List<Pot> potList;

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

        potsAdapter = new PotsAdapter(potList, getContext()); // Use getContext() instead of 'this'
        recyclerView.setAdapter(potsAdapter);

        // Example of creating a default pot using the factory
        Pot defaultPot = PotFactory.createPot("Default", "Default Pot 1", "Green", 200);

        // Add the created pot to the list and notify the adapter
        potList.add(defaultPot);
        potsAdapter.notifyDataSetChanged();

        // Add the plus icon item at the end of the list
        potList.add(null);  // Add a placeholder for the plus item

        return view;
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
