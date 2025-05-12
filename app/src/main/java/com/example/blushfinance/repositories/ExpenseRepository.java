package com.example.blushfinance.repositories; // Or your chosen package for repositories

import android.app.Application; // Required for getting database instance
import androidx.lifecycle.LiveData;

// Adjust these imports to point to your actual class locations
import com.example.blushfinance.db.AppDatabase;
import com.example.blushfinance.db.ExpenseDao;
import com.example.blushfinance.db.ExpenseCard; // Assuming ExpenseCard is in 'db' package

import java.util.List;

/**
 * Repository for handling data operations related to Expenses.
 * This class abstracts the data source (Room database) from the ViewModel.
 */
public class ExpenseRepository {

    private ExpenseDao mExpenseDao;
    private LiveData<List<ExpenseCard>> mAllExpenseCards; // LiveData to observe all expense items

    /**
     * Constructor for the ExpenseRepository.
     * @param application The application context, used to get the database instance.
     */
    public ExpenseRepository(Application application) {
        // Get a handle to the database and the ExpenseDao
        AppDatabase db = AppDatabase.getDatabase(application);
        mExpenseDao = db.expenseDao();
        // Initialize LiveData for all expense cards from the DAO
        mAllExpenseCards = mExpenseDao.getAllExpenseCards();
    }

    /**
     * Retrieves all expense cards as LiveData.
     * This allows the UI to observe changes to the expense data automatically.
     * @return LiveData list of all ExpenseCard objects.
     */
    public LiveData<List<ExpenseCard>> getAllExpenseCards() {
        return mAllExpenseCards;
    }

    /**
     * Inserts a new expense card into the database.
     * This operation is performed on a background thread using the AppDatabase's executor.
     * @param expenseCard The ExpenseCard object to insert.
     */
    public void insert(ExpenseCard expenseCard) {
        // Use the ExecutorService from AppDatabase to run database operations off the main thread
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mExpenseDao.insert(expenseCard);
        });
    }

    /**
     * Deletes an expense card from the database.
     * This operation is performed on a background thread.
     * @param expenseCard The ExpenseCard object to delete.
     */
    public void delete(ExpenseCard expenseCard) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mExpenseDao.delete(expenseCard);
        });
    }

    /**
     * Deletes all expense cards from the database.
     * This operation is performed on a background thread.
     */
    public void deleteAllExpenses() {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            mExpenseDao.deleteAllExpenseCards();
        });
    }

    // You can add more methods here if needed, e.g., update, getById, etc.
}
