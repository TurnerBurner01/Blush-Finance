package com.example.blushfinance.fragments.Pots_Page;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.blushfinance.R;

import java.util.ArrayList;
import java.util.List;

public class PotsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PotsAdapter potsAdapter;
    private List<Pot> potList;  // the list of pots

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.p_activity_pots);

        // Adjust window insets to handle system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.pots), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Grid layout with 2 columns

        potList = new ArrayList<>();  // Initialize pot list

        potsAdapter = new PotsAdapter(potList, this);
        recyclerView.setAdapter(potsAdapter);

        // Example of creating a default pot using the factory
        Pot defaultPot = PotFactory.createPot("Default", "Default Pot 1", "Green", 200);

        // Add the created pot to the list and notify the adapter
        potList.add(defaultPot);
        potsAdapter.notifyDataSetChanged(); // Notify the adapter to update the grid

        // Add the plus icon item at the end of the list
        potList.add(null);  // Add a placeholder for the plus item
    }

    public void addNewPot(Pot newPot) {
        // Remove the plus icon if it's there
        if (!potList.isEmpty() && potList.get(potList.size() - 1) == null) {
            potList.remove(potList.size() - 1);  // Remove the plus pot
        }

        // Add the new pot to the list
        potList.add(newPot);

        // Re-add the plus icon at the end of the list
        potList.add(null);  // 'null' represents the plus icon

        // Notify the adapter that the data has changed
        potsAdapter.notifyDataSetChanged();
    }


}

