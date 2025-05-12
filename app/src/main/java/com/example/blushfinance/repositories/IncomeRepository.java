package com.example.blushfinance.repositories; // Or your chosen package for repositories

import android.app.Application; // Required for getting database instance
import androidx.lifecycle.LiveData;

// Adjust these imports to point to your actual class locations
import com.example.blushfinance.db.AppDatabase;
import com.example.blushfinance.db.IncomeDao;
import com.example.blushfinance.db.IncomeCard; // Assuming IncomeCard is in 'db' package

import java.util.List;

/**
 * Repository for handling data operations related to Income.
 * This class abstracts the data source (Room database) from the ViewModel.
 */
public class IncomeRepository {

    private IncomeDao mIncomeDao;
    private LiveData<List<IncomeCard>> mAllIncomeCards; // LiveData to observe all income items

    /**
     * Constructor for the IncomeRepository.
     * @param application The application context, used to get the database instance.
     */
    public IncomeRepository(Application application) {
        // Get a handle to the database and the IncomeDao
        AppDatabase db = AppDatabase.getDatabase(application);
        mIncomeDao = db.incomeDao();
        // Initialize LiveData for all income cards from the DAO
        mAllIncomeCards = mIncomeDao.getAllIncomeCards();
    }

    /**
     * Retrieves all income cards as LiveData.
     * This allows the UI to observe changes to the income data automatically.
     * @return LiveData list of all IncomeCard objects.
     */
    public LiveData<List<IncomeCard>> getAllIncomeCards() {
        return mAllIncomeCards;
    }

    /**
     * Inserts a new income card into the database.
     * This operation is performed on a background thread using the AppDatabase's executor.
     * @param incomeCard The IncomeCard object to insert.
     */
    public void insert(IncomeCard incomeCard) {
        // Use the ExecutorService from AppDatabase to run database operations off the main thread
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mIncomeDao.insert(incomeCard);
        });
    }

    /**
     * Deletes an income card from the database.
     * This operation is performed on a background thread.
     * @param incomeCard The IncomeCard object to delete.
     */
    public void delete(IncomeCard incomeCard) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mIncomeDao.delete(incomeCard);
        });
    }

    /**
     * Deletes all income cards from the database.
     * This operation is performed on a background thread.
     */
    public void deleteAllIncome() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mIncomeDao.deleteAllIncomeCards();
        });
    }

    // You can add more methods here if needed, e.g., update, getById, etc.
}
