package com.example.blushfinance.db; // Should be in your new 'db' package

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// Adjust these imports to point to your actual Entity class locations
// It's recommended to move IncomeCard and ExpenseCard to the 'db' package or a 'models'/'entities' sub-package

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The main Room database class for the application.
 * This class is a singleton to ensure only one instance of the database is created.
 * It lists all entities (tables) and provides access to their DAOs.
 */
@Database(entities = {IncomeCard.class, ExpenseCard.class}, version = 1, exportSchema = false)
// - entities: Lists all the @Entity classes that define your database tables.
// - version:  The version of your database schema. Increment this if you change the schema
//             and provide a migration strategy.
// - exportSchema: Set to false to avoid a build warning about schema export location,
//                 unless you specifically want to export the schema for version control.
public abstract class AppDatabase extends RoomDatabase {

    // Abstract methods to get instances of your DAOs. Room will generate the implementation.
    public abstract IncomeDao incomeDao();
    public abstract ExpenseDao expenseDao();

    // Singleton instance of the AppDatabase
    private static volatile AppDatabase INSTANCE;

    // ExecutorService for running database operations on a background thread
    private static final int NUMBER_OF_THREADS = 4; // Adjust as needed
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    /**
     * Gets the singleton instance of the AppDatabase.
     * Creates the database if it doesn't exist yet.
     * @param context The application context.
     * @return The singleton AppDatabase instance.
     */
    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            // synchronized block to ensure thread safety during instance creation
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    // Create the database instance using Room's databaseBuilder.
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "blush_finance_db") // Name of your database file
                            // .fallbackToDestructiveMigration() // Use ONLY during development if you change schema and don't want to write migrations yet.
                            // This will WIPE all data on schema version change.
                            // For production, you need to implement proper migrations:
                            // .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Example of a Migration (if you were to change schema from version 1 to 2)
    // static final Migration MIGRATION_1_2 = new Migration(1, 2) {
    //     @Override
    //     public void migrate(SupportSQLiteDatabase database) {
    //         // Example: database.execSQL("ALTER TABLE income_cards ADD COLUMN new_column TEXT");
    //     }
    // };
}
