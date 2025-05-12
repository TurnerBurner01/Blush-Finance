package com.example.blushfinance.fragments.Finance_Page;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.blushfinance.repositories.ExpenseRepository; // Ensure this path is correct
import com.example.blushfinance.db.ExpenseCard; // Ensure this path is correct (points to your entity in 'db' package)

import java.util.ArrayList;
import java.util.List;

public class ExpenseViewModel extends AndroidViewModel {

    private ExpenseRepository mRepository;
    private LiveData<List<ExpenseCard>> actualExpenseCardsFromDb; // This is LiveData<List<db.ExpenseCard>>
    private final MediatorLiveData<List<ExpenseCard>> displayableExpenseCardsList = new MediatorLiveData<>();

    public ExpenseViewModel(Application application) {
        super(application);
        // *** Ensure ExpenseRepository class exists and is imported correctly ***
        mRepository = new ExpenseRepository(application);
        actualExpenseCardsFromDb = mRepository.getAllExpenseCards(); // This returns LiveData<List<db.ExpenseCard>>

        displayableExpenseCardsList.addSource(actualExpenseCardsFromDb, actualCards -> {
            List<ExpenseCard> displayList = new ArrayList<>();
            if (actualCards != null) {
                displayList.addAll(actualCards);
            }
            displayList.add(null); // For the '+' button
            displayableExpenseCardsList.setValue(displayList);
        });
    }

    public LiveData<List<ExpenseCard>> getDisplayableExpenseCardsList() {
        return displayableExpenseCardsList; // Returns LiveData<List<db.ExpenseCard (including null)>>
    }

    // *** THIS IS THE METHOD YOUR FRAGMENT NEEDS ***
    public LiveData<List<ExpenseCard>> getActualCardsLiveData() {
        return actualExpenseCardsFromDb; // Returns LiveData<List<db.ExpenseCard>>
    }

    public void addExpenseCard(ExpenseCard expenseCard) { // Parameter is db.ExpenseCard
        if (expenseCard != null) {
            mRepository.insert(expenseCard);
        }
    }

    public void deleteExpenseCard(ExpenseCard expenseCard) { // Parameter is db.ExpenseCard
        if (expenseCard != null) {
            mRepository.delete(expenseCard);
        }
    }

    public void deleteAllExpenses() {
        mRepository.deleteAllExpenses();
    }
}
