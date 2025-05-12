package com.example.blushfinance.fragments.Finance_Page; // Updated package

import android.app.Application; // Required for AndroidViewModel
import androidx.lifecycle.AndroidViewModel; // Changed from ViewModel
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData; // To transform/combine LiveData

// Adjust these imports to point to your actual class locations
import com.example.blushfinance.repositories.IncomeRepository; // Your IncomeRepository
import com.example.blushfinance.db.IncomeCard; // Assuming IncomeCard is in 'db' package

import java.util.ArrayList;
import java.util.List;

/**
 * ViewModel for managing income data for the IncomeFragment.
 * Uses IncomeRepository to interact with the data source (Room database).
 */
public class IncomeViewModel extends AndroidViewModel { // Extends AndroidViewModel

    private IncomeRepository mRepository;

    // LiveData for the actual income cards fetched from the database via the repository
    private LiveData<List<IncomeCard>> actualIncomeCardsFromDb;

    // LiveData for the list displayed in RecyclerView (actual cards + null for the '+' button)
    // MediatorLiveData allows us to react to changes from actualIncomeCardsFromDb
    private final MediatorLiveData<List<IncomeCard>> displayableIncomeCardsList = new MediatorLiveData<>();

    /**
     * Constructor for IncomeViewModel.
     * @param application The application context, needed to initialize the repository.
     */
    public IncomeViewModel(Application application) {
        super(application);
        // *** Ensure IncomeRepository class exists and is in the correct package ***
        mRepository = new IncomeRepository(application); // Initialize the repository
        actualIncomeCardsFromDb = mRepository.getAllIncomeCards(); // Get LiveData from repository

        // Observe the LiveData from the repository. When it changes,
        // update the displayableIncomeCardsList (which includes the '+' button).
        displayableIncomeCardsList.addSource(actualIncomeCardsFromDb, actualCards -> {
            List<IncomeCard> displayList = new ArrayList<>();
            if (actualCards != null) {
                displayList.addAll(actualCards);
            }
            displayList.add(null); // Add the placeholder for the '+' button
            displayableIncomeCardsList.setValue(displayList);
        });
    }

    /**
     * Returns LiveData containing the list suitable for the RecyclerView adapter.
     * This list includes actual income cards plus a null item representing the add button.
     */
    public LiveData<List<IncomeCard>> getDisplayableIncomeCardsList() {
        return displayableIncomeCardsList;
    }

    /**
     * Returns LiveData of the actual income cards from the database.
     * The Fragment can observe this to update the PieChart or totals.
     */
    public LiveData<List<IncomeCard>> getActualCardsLiveData() {
        return actualIncomeCardsFromDb;
    }

    /**
     * Adds a new income card. This operation is delegated to the repository,
     * which will insert it into the database on a background thread.
     * @param incomeCard The IncomeCard object to add.
     */
    public void addIncomeCard(IncomeCard incomeCard) {
        if (incomeCard != null) {
            mRepository.insert(incomeCard);
            // The LiveData observed by the fragment will automatically update
            // once the database change is reflected in mAllIncomeCards from the repository.
        }
    }

    /**
     * Deletes an income card. Delegated to the repository.
     * @param incomeCard The IncomeCard to delete.
     */
    public void deleteIncomeCard(IncomeCard incomeCard) {
        if (incomeCard != null) {
            mRepository.delete(incomeCard);
        }
    }

    /**
     * Deletes all income entries. Delegated to the repository.
     */
    public void deleteAllIncome() {
        mRepository.deleteAllIncome();
    }
}
