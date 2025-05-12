package com.example.blushfinance.db; // Or your chosen package for database-related files

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

// Adjust this import to point to your actual ExpenseCard model location

import java.util.List;

/**
 * Data Access Object (DAO) for the expense_cards table.
 * Defines methods for interacting with the expense data in the database.
 */
@Dao // Marks this interface as a Room DAO
public interface ExpenseDao {

    /**
     * Inserts a single expense card into the database.
     * If there's a conflict, it replaces the existing entry.
     * @param expenseCard The ExpenseCard object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ExpenseCard expenseCard);

    /**
     * Inserts a list of expense cards into the database.
     * @param expenseCards A list of ExpenseCard objects to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ExpenseCard> expenseCards);

    /**
     * Deletes a specific expense card from the database.
     * @param expenseCard The ExpenseCard object to delete.
     */
    @Delete
    void delete(ExpenseCard expenseCard);

    /**
     * Deletes all expense cards from the table.
     */
    @Query("DELETE FROM expense_cards")
    void deleteAllExpenseCards();

    /**
     * Retrieves all expense cards from the database, ordered by ID.
     * Returns LiveData for reactive UI updates.
     * @return A LiveData list of all ExpenseCard objects.
     */
    @Query("SELECT * FROM expense_cards ORDER BY id ASC")
    LiveData<List<ExpenseCard>> getAllExpenseCards();

    /**
     * Retrieves all expense cards synchronously as a simple List.
     * Use with caution, preferably off the main UI thread.
     * @return A List of all ExpenseCard objects.
     */
    @Query("SELECT * FROM expense_cards ORDER BY id ASC")
    List<ExpenseCard> getAllExpenseCardsList(); // For synchronous fetch if ever needed
}
