package com.example.routinemanager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import java.util.List;
import androidx.lifecycle.MutableLiveData;
import com.example.routinemanager.TimetableEntry; // Replace with your package name
import com.example.routinemanager.TimetableRepository; // Replace with your package name

/**
 * 5. ViewModel
 * This ViewModel provides the data to the UI in a lifecycle-conscious way.
 */
public class TimetableViewModel extends ViewModel {
    private TimetableRepository repository;
    private LiveData<List<TimetableEntry>> allEntries;

    public TimetableViewModel(TimetableRepository repository) {
        this.repository = repository;
        this.allEntries = repository.getAllEntries();
    }

    public LiveData<List<TimetableEntry>> getAllEntries() {
        return allEntries;
    }

    public LiveData<List<TimetableEntry>> getEntriesByDay(String day) {
        return repository.getEntriesByDay(day);
    }

    public LiveData<List<TimetableEntry>> getEntriesByDayOrder(int dayOrder) {
        return repository.getEntriesByDayOrder(dayOrder);
    }

    public LiveData<TimetableEntry> getEntryById(int id) {
        return repository.getEntryById(id);
    }
    public void insert(TimetableEntry entry) {
        repository.insert(entry);
    }

    public void update(TimetableEntry entry) {
        repository.update(entry);
    }

    public void delete(TimetableEntry entry) {
        repository.delete(entry);
    }

    /**
     * Inserts a new TimetableEntry into the database via the repository.
     */

   /** public void insert(TimetableEntry entry) {
        repository.insert(entry);
    } */
}

