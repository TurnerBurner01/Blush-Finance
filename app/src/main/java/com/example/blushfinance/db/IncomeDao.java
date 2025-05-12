package com.example.blushfinance.db; // Or your chosen package for database-related files

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

// Adjust this import to point to your actual IncomeCard model location

import java.util.List;

/**
 * Data Access Object (DAO) for the income_cards table.
 * Defines methods for interacting with the income data in the database.
 */
@Dao // Marks this interface as a Room DAO
public interface IncomeDao {

    /**
     * Inserts a single income card into the database.
     * If there's a conflict (e.g., same primary key), it replaces the existing entry.
     * @param incomeCard The IncomeCard object to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IncomeCard incomeCard);

    /**
     * Inserts a list of income cards into the database.
     * Useful for initial data population or batch inserts.
     * @param incomeCards A list of IncomeCard objects to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<IncomeCard> incomeCards);

    /**
     * Deletes a specific income card from the database.
     * @param incomeCard The IncomeCard object to delete. Room identifies it by its primary key.
     */
    @Delete
    void delete(IncomeCard incomeCard);

    /**
     * Deletes all income cards from the table.
     * Use with caution.
     */
    @Query("DELETE FROM income_cards")
    void deleteAllIncomeCards();

    /**
     * Retrieves all income cards from the database, ordered by their ID in ascending order.
     * Returns the data as LiveData, so the UI can observe changes reactively.
     * @return A LiveData list of all IncomeCard objects.
     */
    @Query("SELECT * FROM income_cards ORDER BY id ASC")
    LiveData<List<IncomeCard>> getAllIncomeCards();

    /**
     * Retrieves all income cards synchronously as a simple List.
     * This should be used carefully and typically off the main UI thread
     * if the dataset is large or the operation is long-running.
     * @return A List of all IncomeCard objects.
     */
    @Query("SELECT * FROM income_cards ORDER BY id ASC")
    List<IncomeCard> getAllIncomeCardsList(); // For synchronous fetch if ever needed
}
